package io.pleo.prop.objects

import com.google.inject.Inject
import javax.sql.DataSource

class NoPropObject
@Inject
constructor(val dataSource: DataSource)
