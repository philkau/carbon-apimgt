/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.rest.api.authenticator.factories;


import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.apimgt.core.exception.APIMgtDAOException;
import org.wso2.carbon.apimgt.core.exception.IdentityProviderException;
import org.wso2.carbon.apimgt.rest.api.authenticator.AuthenticatorService;

public class AuthenticatorAPIFactoryTestCase {
    @Test
    public void testGetInstance() {
        //// returns same instance
        AuthenticatorAPIFactory expectedInstance = AuthenticatorAPIFactory.getInstance();
        AuthenticatorAPIFactory actualInstance = AuthenticatorAPIFactory.getInstance();
        Assert.assertSame(expectedInstance, actualInstance);
        Assert.assertNotNull(expectedInstance);
    }

    @Test
    public void getService() throws APIMgtDAOException, IdentityProviderException {
        AuthenticatorService expectedService = AuthenticatorAPIFactory.getInstance().getService();
        AuthenticatorService actualService = AuthenticatorAPIFactory.getInstance().getService();
        Assert.assertSame(expectedService, actualService);
        Assert.assertNotNull(expectedService);
    }
}
