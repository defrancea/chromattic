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

package org.chromattic.spi.type;

import org.chromattic.api.TypeConversionException;

import java.io.InputStream;
import java.util.Calendar;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class ValueType<I, E> {

  private ValueType() {
  }

  public abstract static class STRING<E> extends ValueType<String, E> {
    protected STRING() {
    }
  }

  public abstract static class BINARY<E> extends ValueType<InputStream, E> {
    protected BINARY() {
    }
  }

  public abstract static class LONG<E> extends ValueType<Long, E> {
    protected LONG() {
    }
  }

  public abstract static class DOUBLE<E> extends ValueType<Double, E> {
    protected DOUBLE() {
    }
  }

  public abstract static class DATE<E> extends ValueType<Calendar, E> {
    protected DATE() {
    }
  }

  public abstract static class BOOLEAN<E> extends ValueType<Boolean, E> {
    protected BOOLEAN() {
    }
  }

  public abstract static class NAME<E> extends ValueType<String, E> {
    protected NAME() {
    }
  }

  public abstract static class PATH<E> extends ValueType<String, E> {
    protected PATH() {
    }
  }

  public abstract Class<E> getExternalType();

  public abstract I getInternal(E e) throws TypeConversionException;

  public abstract E getExternal(I i) throws TypeConversionException;

  public abstract E fromString(String s) throws TypeConversionException;

  public abstract String toString(E e) throws TypeConversionException;

}