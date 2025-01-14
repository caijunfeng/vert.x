/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.core.net;

import io.netty.handler.ssl.SslProvider;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.tls.DefaultSslContextFactory;
import io.vertx.core.spi.tls.SslContextFactory;

import javax.net.ssl.SSLEngine;

/**
 * Configures a {@link TCPSSLOptions} to use the JDK ssl engine implementation.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class JdkSSLEngineOptions extends SSLEngineOptions {

  private static Boolean jdkAlpnAvailable;

  /**
   * @return if alpn support is available via the JDK SSL engine
   */
  public static synchronized boolean isAlpnAvailable() {
    if (jdkAlpnAvailable == null) {
      boolean available = false;
      try {
        // Public method on SSLEngine
        SSLEngine.class.getDeclaredMethod("getApplicationProtocol");
        available = true;
      } catch (Exception ignore1) {
        // JDK provides ALPN
        try {
          // Always use bootstrap class loader.
          JdkSSLEngineOptions.class.getClassLoader().loadClass("sun.security.ssl.ALPNExtension");
           available = true;
        } catch (Exception ignore2) {
          // alpn-boot was not loaded.
        }
      } finally {
        jdkAlpnAvailable = available;
      }
    }
    return jdkAlpnAvailable;
  }

  public JdkSSLEngineOptions() {
  }

  public JdkSSLEngineOptions(JsonObject json) {
  }

  public JdkSSLEngineOptions(JdkSSLEngineOptions that) {
    super(that);
  }

  @Override
  public JdkSSLEngineOptions setUseWorkerThread(boolean useWorkerThread) {
    return (JdkSSLEngineOptions) super.setUseWorkerThread(useWorkerThread);
  }

  public JsonObject toJson() {
    return new JsonObject();
  }

  @Override
  public JdkSSLEngineOptions copy() {
    return new JdkSSLEngineOptions(this);
  }

  @Override
  public SslContextFactory sslContextFactory() {
    return new DefaultSslContextFactory(SslProvider.JDK, false);
  }
}
