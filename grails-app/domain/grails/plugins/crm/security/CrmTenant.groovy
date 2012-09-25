package grails.plugins.crm.security

import grails.plugins.crm.core.AuditEntity

/**
 * This domain class represents a tenant, also known as "account".
 * A user can be associated with multiple tenants but only have one tenant active at a given time.
 *
 * @author Goran Ehrsson
 * @since 0.1
 */
@AuditEntity
class CrmTenant {

    // Long id of this account will be used as tenantId for all instances created by this tenant.
    java.sql.Date expires
    String locale
    String name
    CrmTenant parent
    static belongsTo = [user: CrmUser]
    static hasMany = [options: CrmTenantOption]
    static constraints = {
        locale(maxSize:5, nullable:true, blank:false)
        expires(nullable:true)
        name(size: 3..80, maxSize: 80, nullable: false, blank: false, unique:'user')
        parent(nullable:true)
    }
    static mapping = {
        table 'crm_tenant'
        sort 'name'
        cache 'nonstrict-read-write'
    }

    static transients = ['dao', 'option', 'children']

    /**
     * Returns the name property.
     * @return name property
     */
    String toString() {
        name
    }

    List<CrmTenant> getChildren() {
        CrmTenant.findAllByParent(this)
    }

    /**
     * Clients should use this method to get tenant properties instead of accessing the domain instance directly.
     * The following properties are returned as a Map: [Long id, String name, Map user [username, name, email]]
     * @return a data access object (Map) representing the domain instance.
     */
    Map<String, Object> getDao() {
        [id: id, name: name, parent: parent?.id, locale: locale ? new Locale(*locale.split('_')) : Locale.getDefault(),
                user: [id:user.id, username: user.username, name: user.name, email: user.email],
                options: getOptionsMap(), dateCreated: dateCreated, expires:expires]
    }

    /**
     * Return tenant parameters (options) as a Map.
     *
     * @return options
     */
    private Map<String, Object> getOptionsMap() {
        options.inject([:]) {map, o->
            map[o.key] = o.value
            map
        }
    }

    void setOption(String key, Object value) {
        if(value == null) {
            removeOption(key)
        } else {
            def o = options.find{it.key == key}
            if(o) {
                o.value = value
            } else {
                o = new CrmTenantOption(key, value)
                addToOptions(o)
            }
        }
    }

    def getOption(String key) {
        options.find{it.key == key}?.value
    }

    boolean removeOption(String key) {
        def o = options.find{it.key == key}
        if(o) {
            removeFromOptions(o)
            return true
        }
        return false
    }
}
