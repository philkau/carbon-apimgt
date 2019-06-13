/*
 *  Copyright WSO2 Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.apimgt.impl.template;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIProduct;
import org.wso2.carbon.apimgt.api.model.APIProductResource;
import org.wso2.carbon.apimgt.api.model.URITemplate;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Set the uri templates as the resources
 */
public class ResourceConfigContext extends ConfigContextDecorator {
    //private static final Log log = LogFactory.getLog(ResourceConfigContext.class);

    private API api;
    private APIProduct apiProduct;
    private String faultSeqExt;

    public ResourceConfigContext(ConfigContext context, API api) {
        super(context);
        this.api = api;
    }

    public ResourceConfigContext(ConfigContext context, APIProduct apiProduct) {
        super(context);
        this.apiProduct = apiProduct;
    }

    public void validate() throws APIManagementException {
        if (api != null) {
            if (api.getUriTemplates() == null || api.getUriTemplates().isEmpty()) {
                throw new APIManagementException("At least one resource is required");
            }
            if (APIUtil.isSequenceDefined(api.getFaultSequence())) {
                String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
                if (api.getId().getProviderName().contains("-AT-")) {
                    String provider = api.getId().getProviderName().replace("-AT-", "@");
                    tenantDomain = MultitenantUtils.getTenantDomain(provider);
                }
                int tenantId;
                try {
                    tenantId = ServiceReferenceHolder.getInstance().getRealmService().
                            getTenantManager().getTenantId(tenantDomain);
                    if (APIUtil.isPerAPISequence(api.getFaultSequence(), tenantId, api.getId(),
                            APIConstants.API_CUSTOM_SEQUENCE_TYPE_FAULT)) {
                        this.faultSeqExt = APIUtil.getSequenceExtensionName(api) + APIConstants.API_CUSTOM_SEQ_FAULT_EXT;
                    }
                } catch (UserStoreException e) {
                    throw new APIManagementException("Error while retrieving tenant Id from " +
                            api.getId().getProviderName(), e);
                } catch (APIManagementException e) {
                    throw new APIManagementException("Error while checking whether sequence " + api.getFaultSequence() +
                            " is a per API sequence.", e);
                }
            }
        }
    }

    public VelocityContext getContext() {
        VelocityContext context = super.getContext();

        if (api != null) {
            context.put("resources", api.getUriTemplates());
            context.put("apiStatus", api.getStatus());
            context.put("faultSequence", faultSeqExt != null ? faultSeqExt : api.getFaultSequence());
        } else if (apiProduct != null) {
            List<APIProductResource> productResources = apiProduct.getProductResources();

            List<URITemplate> uriTemplates = new ArrayList<>();
            for (APIProductResource productResource : productResources) {
                uriTemplates.add(productResource.getUriTemplate());
            }

            context.put("resources", uriTemplates);
        }

        return context;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
