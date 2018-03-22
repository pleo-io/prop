package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.sql.DataSource;

public class NoPropObject {
  private DataSource dataSource;

  @Inject
  public NoPropObject(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public DataSource getDataSource() {
    return dataSource;
  }
}
