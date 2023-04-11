/*
 * Licensed to Crate.io GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.sql.tree;

import java.util.Objects;

public abstract class AbstractCast extends Expression {
    private final Expression expression;
    private final ColumnType<?> type;

    private final boolean isIntegerOnly;

    protected AbstractCast(Expression expression, ColumnType<?> type) {
        this(expression, type, false);
    }

    protected AbstractCast(Expression expression, ColumnType<?> type, boolean isIntegerOnly) {
        this.expression = expression;
        this.type = type;
        this.isIntegerOnly = isIntegerOnly;
    }

    public Expression getExpression() {
        return expression;
    }

    public ColumnType<?> getType() {
        return type;
    }

    public boolean isIntegerOnly() {
        return isIntegerOnly;
    }

    @Override
    public abstract <R, C> R accept(AstVisitor<R, C> visitor, C context);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractCast cast = (AbstractCast) o;
        return Objects.equals(expression, cast.expression) &&
               Objects.equals(type, cast.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, type);
    }
}
