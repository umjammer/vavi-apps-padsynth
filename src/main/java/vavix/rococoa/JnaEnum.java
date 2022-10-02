/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa;

/**
 * JnaEnum.
 *
 * TODO doesn't work
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-04 nsano initial version <br>
 * @see "http://technofovea.com/blog/archives/815"
 */
public interface JnaEnum<T> {
    int getIntValue();

    T getForValue(int i);
}
