/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.chromattic.metamodel.mapping2;

import org.chromattic.metamodel.bean2.SimpleValueInfo;
import org.chromattic.metamodel.bean2.SingleValuedPropertyInfo;
import org.chromattic.metamodel.mapping.NodeAttributeType;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class AttributeMapping extends PropertyMapping<SingleValuedPropertyInfo<SimpleValueInfo>, SimpleValueInfo> {

  /** . */
  private final NodeAttributeType type;

  public AttributeMapping(SingleValuedPropertyInfo<SimpleValueInfo> property, NodeAttributeType type) {
    super(property);
    this.type = type;
  }

  public NodeAttributeType getType() {
    return type;
  }

  @Override
  public void accept(MappingVisitor visitor) {
    throw new UnsupportedOperationException();
  }
}