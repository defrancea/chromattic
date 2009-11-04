/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.chromattic.core.bean;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PrimitiveSimpleType<T> extends SimpleType<T> {

  /** . */
  public static final SimpleType<Integer> INT = new PrimitiveSimpleType<Integer>(SimpleTypeKind.INT, Integer.class, int.class);

  /** . */
  public static final SimpleType<Boolean> BOOLEAN = new PrimitiveSimpleType<Boolean>(SimpleTypeKind.BOOLEAN, Boolean.class, boolean.class);

  /** . */
  public static final SimpleType<Long> LONG = new PrimitiveSimpleType<Long>(SimpleTypeKind.LONG, Long.class, long.class);

  /** . */
  public static final SimpleType<Double> DOUBLE = new PrimitiveSimpleType<Double>(SimpleTypeKind.DOUBLE, Double.class, double.class);

  /** . */
  public static final SimpleType<Float> FLOAT = new PrimitiveSimpleType<Float>(SimpleTypeKind.FLOAT, Float.class, float.class);

  public PrimitiveSimpleType(SimpleTypeKind kind, Class<T> javaType, Class<?> listElementType) {
    super(kind, javaType, listElementType);
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}