/*******************************************************************************
 * Copyright (c) 2018, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.security.jwt.fat.builder.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.ibm.json.java.JSONObject;
import com.ibm.websphere.simplicity.log.Log;
import com.ibm.ws.security.fat.common.actions.TestActions;
import com.ibm.ws.security.fat.common.utils.SecurityFatHttpUtils;
import com.ibm.ws.security.jwt.fat.builder.JWTBuilderConstants;
import com.ibm.ws.security.jwt.fat.builder.utils.BuilderHelpers;
import com.meterware.httpunit.Base64;

import componenttest.topology.impl.LibertyServer;

public class JwtBuilderActions extends TestActions {
    protected static Class<?> thisClass = JwtBuilderActions.class;

    public static final String ACTION_INVOKE_JWT_BUILDER = "invokeJwtBuilder";

    public Page invokeJwtBuilder_create(String testName, LibertyServer server, String builderId) throws Exception {
        return invokeJwtBuilder(testName, server, builderId, JWTBuilderConstants.JWT_BUILDER_CREATE_ENDPOINT, null);

    }

    public Page invokeJwtBuilder_setApis(String testName, LibertyServer server, String builderId) throws Exception {

        return invokeJwtBuilder(testName, server, builderId, JWTBuilderConstants.JWT_BUILDER_SETAPIS_ENDPOINT, null);

    }

    public Page invokeJwtBuilder_setApis(String testName, LibertyServer server, String builderId, JSONObject attrs) throws Exception {

        List<NameValuePair> parms = new ArrayList<NameValuePair>();
        return invokeJwtBuilder_setApis(testName, server, builderId, parms, attrs);

    }

    // adds the claim list to the list of parms (those are additional parms)
    public Page invokeJwtBuilder_setApis(String testName, LibertyServer server, String builderId, List<NameValuePair> parms, JSONObject attrs) throws Exception {

        parms.add(new NameValuePair("attrs", attrs.toString()));

        return invokeJwtBuilder(testName, server, builderId, JWTBuilderConstants.JWT_BUILDER_SETAPIS_ENDPOINT, parms);

    }

    public Page invokeJwtBuilder(String testName, LibertyServer server, String builderId, String endpoint, List<NameValuePair> parms) throws Exception {

        String jwtBuilderUrl = SecurityFatHttpUtils.getServerUrlBase(server) + endpoint;

        List<NameValuePair> requestParams = setRequestParms(builderId, parms);

        WebClient webClient = new WebClient();
        Page response = null;
        try {
            response = invokeUrlWithParameters(testName, webClient, jwtBuilderUrl, requestParams);
            return response;
        } catch (Exception e) {
            Log.info(thisClass, "invokeJwtBuilder", e.getMessage());
            throw e;
        } finally {
            destroyWebClient(webClient);
        }

    }

    public List<NameValuePair> setRequestParms(String builderId, List<NameValuePair> parms) throws Exception {

        List<NameValuePair> requestParms = new ArrayList<NameValuePair>();
        if (builderId != null) {
            requestParms.add(new NameValuePair(JWTBuilderConstants.JWT_BUILDER_PARAM_BUILDER_ID, builderId));
        }

        if (parms != null) {
            for (NameValuePair parm : parms) {
                Log.info(thisClass, "setRequestParm", "Setting: " + parm.getName() + " value: " + parm.getValue());
                requestParms.add(new NameValuePair(parm.getName(), parm.getValue()));
            }
        }
        return requestParms;
    }

    public Page invokeProtectedAppWithJwtTokenAsParm(String testcase, Object response, String app) throws Exception {
        // pull out the token and then invoke the app on the RS server
        String jwtToken = BuilderHelpers.extractJwtTokenFromResponse(response, JWTBuilderConstants.BUILT_JWT_TOKEN);

        List<NameValuePair> requestParms = new ArrayList<NameValuePair>();
        requestParms.add(new NameValuePair("access_token", jwtToken));
        Page appResponse = invokeUrlWithParametersUsingPost(testcase, app, requestParms);

        return appResponse;
    }

    public Page invokeProtectedAppWithJwtTokenInHeader(String testcase, Object response, String app) throws Exception {
        // pull out the token and then invoke the app on the RS server
        String jwtToken = BuilderHelpers.extractJwtTokenFromResponse(response, JWTBuilderConstants.BUILT_JWT_TOKEN);

        Page appResponse = invokeUrlWithBearerTokenUsingPost(testcase, app, jwtToken);

        return appResponse;
    }

    public Page invokeProtectedJwtBuilder(String testName, LibertyServer server, String builderId, JSONObject attrs,
            String user, String pw) throws Exception {

        String jwtBuilderUrl = SecurityFatHttpUtils.getServerUrlBase(server)
                + JWTBuilderConstants.JWT_BUILDER_PROTECTED_SETAPIS_ENDPOINT;

        // Create request params
        List<NameValuePair> parms = new ArrayList<NameValuePair>();
        parms.add(new NameValuePair("attrs", attrs.toString()));
        List<NameValuePair> requestParams = setRequestParms(builderId, parms);

        // Create request headers with basic auth
        Map<String, String> requestHeaders = new HashMap<>();
        String encodedIdPw = Base64.encode(user + ":" + pw);
        requestHeaders.put("Authorization", "Basic " + encodedIdPw);

        WebClient webClient = new WebClient();
        Page response = null;
        try {
            response = invokeUrlWithParametersAndHeaders(testName, webClient, jwtBuilderUrl, requestParams,
                    requestHeaders);
            return response;
        } catch (Exception e) {
            Log.info(thisClass, "invokeJwtBuilder", e.getMessage());
            throw e;
        } finally {
            destroyWebClient(webClient);
        }

    }

}
