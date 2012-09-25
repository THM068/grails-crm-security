/*
 * Copyright (c) 2012 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.crm.security

import grails.plugins.crm.core.SearchUtils

/**
 * User administration service.
 */
class CrmUserService {

    static transactional = true

    /**
     * Empty query = search all records.
     *
     * @param params pagination parameters
     * @return List of CrmUser domain instances
     */
    def list(Map params) {
        list([:], params)
    }

    /**
     * Find CrmUser instances filtered by query.
     *
     * @param query filter parameters
     * @param params pagination parameters
     * @return List of CrmUser domain instances
     */
    def list(Map query, Map params) {

        CrmUser.createCriteria().list(params) {
            if (query.username) {
                ilike('username', SearchUtils.wildcard(query.username))
            }
            if (query.name) {
                ilike('name', SearchUtils.wildcard(query.name))
            }
            if (query.email) {
                ilike('email', SearchUtils.wildcard(query.email))
            }
        }
    }
}
