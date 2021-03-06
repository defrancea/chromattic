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

package org.chromattic.docs.reference.motivation;

import org.chromattic.core.api.ChromatticSessionImpl;
import org.chromattic.docs.reference.AbstractTestCase;
import org.chromattic.ext.ntdef.*;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.Arrays;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class MotivationTestCase extends AbstractTestCase {

  @Override
  protected Iterable<Class<?>> classes() {
    return Arrays.asList(NTFolder.class, NTFile.class, NTHierarchyNode.class, NTResource.class);
  }

  public void testFoo() {

    ChromatticSessionImpl session = login();

    try {

      NTFolder root = session.insert(NTFolder.class, "root");
      root.createFile("a.txt", Resource.createPlainText("the content of a"));
      NTFolder b = root.createFolder("b");
      b.createFile("c.txt", Resource.createPlainText("the content of c"));

      list(root);

      try {
        list(session.getNode(root));
      }
      catch (RepositoryException e) {
        e.printStackTrace();
      }

    } finally {
      session.close();
    }
  }

  private void list(Node node) throws RepositoryException {
    if (!node.isNodeType("nt:hierarchyNode")) {
      throw new IllegalArgumentException("The provided node is not a hierarchy node");
    }
    if (node.isNodeType("nt:file")) {
      if (node.hasNode("jcr:content")) {
        Node content = node.getNode("jcr:content");
        String encoding = null;
        if (content.hasProperty("jcr:encoding")) {
          encoding = content.getProperty("jcr:encoding").getString();
        }
        String mimeType = content.getProperty("jcr:mimeType").getString();
        System.out.println("File[name=" + node.getName() + ",mime-type=" + mimeType +
          ",encoding=" + encoding +  "]");
      }
    } else if (node.isNodeType("nt:folder")) {
      System.out.println("Folder[" + node.getName() + "]");
      for (NodeIterator i = node.getNodes();i.hasNext();) {
        list(i.nextNode());
      }
    }
  }

  private void list(NTHierarchyNode hierarchy) {
    if (hierarchy instanceof NTFile) {
      NTFile file = (NTFile)hierarchy;
      Resource content = file.getContentResource();
      if (content != null) {
        System.out.println("File[name=" + file.getName() + ",mime-type=" + content.getMimeType() +
          ",encoding=" + content.getEncoding() +  "]");
      }
    } else {
      NTFolder folder = (NTFolder)hierarchy;
      System.out.println("Folder[" + folder.getName() + "]");
      for (NTHierarchyNode child : folder.getChildren().values()) {
        list(child);
      }
    }
  }
}
