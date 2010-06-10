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

package org.chromattic.metamodel.typegen;

import java.io.Writer;
import java.util.*;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class NodeTypeSerializer {

  /** . */
  private final List<NodeType> nodeTypes;

  /** . */
  private final Map<String, String> mappings;

  public NodeTypeSerializer(List<NodeType> nodeTypes, Map<String, String> mappings) {
    if (nodeTypes == null) {
      throw new NullPointerException();
    }

    //
    this.nodeTypes = new ArrayList<NodeType>(nodeTypes);
    this.mappings = new HashMap<String, String>(mappings);
  }

  public NodeTypeSerializer(List<NodeType> nodeTypes) {
    this(nodeTypes, Collections.<String, String>emptyMap());
  }

  public NodeTypeSerializer(Map<String, String> mappings) {
    this(Collections.<NodeType>emptyList(), mappings);
  }

  protected NodeTypeSerializer() {
    this(Collections.<NodeType>emptyList(), Collections.<String, String>emptyMap());
  }

  public void addNodeType(NodeType nodeType) {
    if (nodeType == null) {
      throw new NullPointerException("No node type provided");
    }
    nodeTypes.add(nodeType);
  }

  public void addPrefixMapping(String namespacePrefix, String namespaceURI) {
    if (namespacePrefix == null) {
      throw new NullPointerException("No null namespace prefix accepted");
    }
    if (namespaceURI == null) {
      throw new NullPointerException("No null namespace uri accepted");
    }

    //
    mappings.put(namespacePrefix, namespaceURI);
  }

  public abstract void writeTo(Writer writer) throws Exception;

  public final void writeTo() throws Exception {
    startNodeTypes(Collections.unmodifiableMap(mappings));

    //
    for (NodeType nodeType : nodeTypes) {
      if (!nodeType.getSkip()) {
        LinkedHashSet<String> superTypeNames = new LinkedHashSet<String>();

        //
        if (nodeType.declaredSuperTypes.isEmpty()) {
          superTypeNames.add("nt:base");
        }
        for (NodeType superType : nodeType.declaredSuperTypes) {
          superTypeNames.add(superType.getName());
        }
        // Add nt:base and mix:referenceable
        superTypeNames.add("mix:referenceable");

        //
        startNodeType(
          nodeType.mapping.getType().getName(),
          nodeType.getName(),
          nodeType.isMixin(),
          nodeType.isOrderable(),
          superTypeNames
        );

        //
        startProperties();

        //
        for (PropertyDefinition propertyDefinition : nodeType.getPropertyDefinitions().values()) {
          property(
            propertyDefinition.getName(),
            propertyDefinition.getType(),
            propertyDefinition.isMultiple(),
            propertyDefinition.getDefaultValues()
          );
        }

        //
        endProperties();

        //
        startChildNodes();

        //
        for (NodeDefinition childNodeDefinition : nodeType.getChildNodeDefinitions().values()) {
          childNode(
            childNodeDefinition.getName(),
            childNodeDefinition.getNodeTypeName(),
            childNodeDefinition.isMandatory(),
            childNodeDefinition.isAutocreated());
        }

        //
        endChildNodes();

        //
        endNodeType();
      }
    }

    //
    endNodeTypes();
  }

  public void startNodeTypes(Map<String, String> mappings) throws Exception {
  }

  public void startNodeType(
    String javaClassName,
    String name,
    boolean mixin,
    boolean orderableChildNodes,
    Collection<String> superTypeNames) throws Exception {
  }

  public void startProperties() throws Exception {
  }

  public void property(
    String name,
    int requiredType,
    boolean multiple,
    Collection<String> defaultValues) throws Exception {
  }

  public void endProperties() throws Exception {
  }

  public void startChildNodes() throws Exception {
  }

  public void childNode(
    String name,
    String nodeTypeName,
    boolean mandatory,
    boolean autocreated) throws Exception {
  }

  public void endChildNodes() throws Exception {
  }

  public void endNodeType() throws Exception {
  }

  public void endNodeTypes() throws Exception {
  }
}
