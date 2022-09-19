/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE file at the root of the source
 * tree and available online at
 *
 * https://github.com/keeps/roda
 */
package org.roda.core.data.exceptions;

/**
 * @author Miguel Guimarãese <mguimaraes@keep.pt>
 */
public class SolrRetryException extends RODAException {
  private static final long serialVersionUID = 3920079022473638105L;

  public SolrRetryException() {
    super();
  }

  public SolrRetryException(String message) {
    super(message);
  }

  public SolrRetryException(String message, Throwable cause) {
    super(message, cause);
  }
}
