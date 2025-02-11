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

package io.crate.execution.dml.upsert;

import io.crate.data.Input;
import io.crate.execution.engine.collect.CollectExpression;
import io.crate.expression.InputFactory;
import io.crate.expression.reference.Doc;
import io.crate.expression.reference.DocRefResolver;
import io.crate.expression.symbol.Symbol;
import io.crate.metadata.NodeContext;
import io.crate.metadata.TransactionContext;
import io.crate.metadata.doc.DocTableInfo;

import org.jetbrains.annotations.Nullable;
import java.util.List;


final class ReturnValueGen {

    private final List<CollectExpression<Doc, ?>> expressions;
    private final List<Input<?>> inputs;

    ReturnValueGen(TransactionContext txnCtx, NodeContext nodeCtx, DocTableInfo table, Symbol[] returnValues) {
        InputFactory.Context<CollectExpression<Doc, ?>> cntx = new InputFactory(nodeCtx).ctxForRefs(
            txnCtx, new DocRefResolver(table.partitionedBy()));
        cntx.add(List.of(returnValues));
        this.expressions = cntx.expressions();
        this.inputs = cntx.topLevelInputs();
    }

    @Nullable
    Object[] generateReturnValues(Doc doc) {
        if (inputs.isEmpty()) {
            return null;
        }
        expressions.forEach(x -> x.setNextRow(doc));
        Object[] result = new Object[inputs.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = inputs.get(i).value();
        }
        return result;
    }
}
