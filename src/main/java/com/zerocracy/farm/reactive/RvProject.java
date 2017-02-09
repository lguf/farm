/**
 * Copyright (c) 2016-2017 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.farm.reactive;

import com.zerocracy.jstk.Item;
import com.zerocracy.jstk.Project;
import com.zerocracy.jstk.Stakeholder;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Reactive project.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
final class RvProject implements Project {

    /**
     * Origin project.
     */
    private final Project origin;

    /**
     * The spin.
     */
    private final Spin spin;

    /**
     * Ctor.
     * @param pkt Project
     * @param list List of stakeholders
     */
    RvProject(final Project pkt, final Stakeholder... list) {
        this(pkt, Arrays.asList(list));
    }

    /**
     * Ctor.
     * @param pkt Project
     * @param list List of stakeholders
     */
    RvProject(final Project pkt, final Collection<Stakeholder> list) {
        this(pkt, new Spin(pkt, list));
    }

    /**
     * Ctor.
     * @param pkt Project
     * @param spn Spin
     */
    RvProject(final Project pkt, final Spin spn) {
        this.origin = pkt;
        this.spin = spn;
    }

    @Override
    public String toString() {
        return this.origin.toString();
    }

    @Override
    public Item acq(final String file) throws IOException {
        Item item = this.origin.acq(file);
        if ("claims.xml".equals(file)) {
            item = new RvItem(item, this.spin);
        }
        return item;
    }

}
