package org.eclipse.aether.util.graph.selector;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;

/**
 * A dependency selector that excludes transitive optional dependencies.
 *
 * @see Dependency#isOptional()
 */
public final class OptionalDependencySelector
    implements DependencySelector
{

    private final boolean transitive;

    /**
     * Creates a new selector to exclude optional transitive dependencies.
     */
    public OptionalDependencySelector()
    {
        this( false );
    }

    private OptionalDependencySelector( final boolean transitive )
    {
        super();
        this.transitive = transitive;
    }

    public boolean selectDependency( Dependency dependency )
    {
        return !this.transitive || !dependency.isOptional();
    }

    public DependencySelector deriveChildSelector( DependencyCollectionContext context )
    {
        OptionalDependencySelector child = this;

        if ( context.getDependency() != null && !child.transitive )
        {
            child = new OptionalDependencySelector( true );
        }
        if ( context.getDependency() == null && child.transitive )
        {
            child = new OptionalDependencySelector( false );
        }

        return child;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        else if ( null == obj || !getClass().equals( obj.getClass() ) )
        {
            return false;
        }

        OptionalDependencySelector that = (OptionalDependencySelector) obj;
        return this.transitive == that.transitive;
    }

    @Override
    public int hashCode()
    {
        int hash = getClass().hashCode();
        hash = hash * 31 + ( this.transitive ? 1 : 0 );
        return hash;
    }

}
