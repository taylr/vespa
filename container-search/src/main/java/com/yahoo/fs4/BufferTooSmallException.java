// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
// -*- mode: java; folded-file: t; c-basic-offset: 4 -*-
//
//

package com.yahoo.fs4;
/**
 * Signal that the buffer used to hold a packet is too small
 *
 * @author  <a href="mailto:borud@yahoo-inc.com">Bjorn Borud</a>
 */
@SuppressWarnings("serial")
public class BufferTooSmallException extends Exception {
    public BufferTooSmallException (String message) {
        super(message);
    }
}
