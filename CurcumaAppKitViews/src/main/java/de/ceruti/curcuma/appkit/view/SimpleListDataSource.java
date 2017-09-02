package de.ceruti.curcuma.appkit.view;

import java.util.List;


public class SimpleListDataSource extends AbstractListDataSource
{

  private List elements;
  
  public SimpleListDataSource(List elements)
  {
    this.elements = elements;
  }

  @Override
  protected List getBackingArray()
  {
    return this.elements;
  }

}
