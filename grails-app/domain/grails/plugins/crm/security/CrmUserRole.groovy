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

/**
 * This domain class connect users and roles.
 * A user can have multiple roles in multiple accounts (tenants).
 * This design reaches beyond traditional multi-tenancy support where a user can only access one tenant.
 *
 * @author Goran Ehrsson
 * @since 0.1
 */
class CrmUserRole {

    CrmRole role
    java.sql.Date expires
    static belongsTo = [user: CrmUser]
    static constraints = {
        role(validator: {val, obj->
            def tenantExpires = CrmTenant.withNewSession { CrmTenant.get(obj.role?.tenantId)?.expires }
            if (obj.expires && tenantExpires && obj.expires > tenantExpires) {
                return ['expires.after.tenant', obj.expires, tenantExpires]
            }
            return null
        })
        expires(nullable:true)
    }
    static mapping = {
        table 'crm_user_role'
    }
    String toString() {
        role.toString()
    }
}
