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

import grails.plugins.crm.core.TenantUtils

/**
 * CRM Account related tag libraries.
 */
class CrmSecurityTagLib {

    static namespace = "crm"

    def crmSecurityService

    def permissionList = {attrs, body ->
        def permissions = attrs.permission ?: attrs.permissions
        if (!(permissions instanceof Collection)) {
            permissions = [permissions]
        }
        int i = 0
        for (p in permissions) {
            def map = [(attrs.var ?: 'it'): [label: message(code: p, default: p), permission: p]]
            if (attrs.status) {
                map[attrs.status] = i++
            }
            out << body(map)
        }
    }

    def userRoles = {attrs, body ->
        def tenant = attrs.tenant ?: TenantUtils.tenant
        def username = attrs.username ?: crmSecurityService.currentUser?.username
        def result = CrmUserRole.createCriteria().list() {
            role {
                eq('tenantId', tenant)
            }
            user {
                eq('username', username)
            }
        }.collect{
            def role = it.role
            [role: role.name, expires: it.expires, param:role.param, permissions: role.permissions.flatten()]
        }
        int i = 0
        for (r in result) {
            def map = [(attrs.var ?: 'it'): r]
            if (attrs.status) {
                map[attrs.status] = i++
            }
            out << body(map)
        }
    }

    def userPermissions = {attrs, body ->
        def tenant = attrs.tenant ?: TenantUtils.tenant
        def username = attrs.username ?: crmSecurityService.currentUser?.username
        def result = CrmUserPermission.createCriteria().list() {
            projections {
                property('permissionsString')
            }
            eq('tenantId', tenant)
            user {
                eq('username', username)
            }
        }
        int i = 0
        for (p in result) {
            def map = [(attrs.var ?: 'it'): p]
            if (attrs.status) {
                map[attrs.status] = i++
            }
            out << body(map)
        }
    }
}
