package io.pleo.prop.objects

import javax.inject.Inject
import javax.sql.DataSource

class NoPropObject
    @Inject
    constructor(val dataSource: DataSource)
