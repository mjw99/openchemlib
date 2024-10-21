/*
* Copyright (c) 1997 - 2016
* Actelion Pharmaceuticals Ltd.
* Gewerbestrasse 16
* CH-4123 Allschwil, Switzerland
*
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this
*    list of conditions and the following disclaimer.
* 2. Redistributions in binary form must reproduce the above copyright notice,
*    this list of conditions and the following disclaimer in the documentation
*    and/or other materials provided with the distribution.
* 3. Neither the name of the the copyright holder nor the
*    names of its contributors may be used to endorse or promote products
*    derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
* ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
*/

package com.actelion.research.chem.descriptor;

import com.actelion.research.chem.SSSearcherWithIndex;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

abstract public class AbstractDescriptorHandlerLongFP<U> implements DescriptorHandler<long[], U> {
    protected static final long[] FAILED_OBJECT = new long[0];

    public String encode(long[] o) {
        return calculationFailed(o) ? FAILED_STRING
                                    : new String(new DescriptorEncoder().encodeLong(o), StandardCharsets.UTF_8);
    	}

    public long[] decode(String s) {
        return s == null ?               null
             : s.equals(FAILED_STRING) ? FAILED_OBJECT
             :                           new DescriptorEncoder().decodeLong(s);
    	}

    public long[] decode(byte[] bytes) {
        return bytes == null ?           			null
             : Arrays.equals(bytes, FAILED_BYTES) ? FAILED_OBJECT
             :                        				new DescriptorEncoder().decodeLong(bytes);
    	}

    public boolean calculationFailed(long[] o) {
        return o==null || o.length == 0;
    	}

    public float getSimilarity(long[] o1, long[] o2) {
        return o1 == null
            || o2 == null
            || o1.length == 0
            || o2.length == 0 ? 0.0f
               : SSSearcherWithIndex.getSimilarityTanimoto(o1, o2);
    	}

/*	public long[][] invertDescriptors(long[][] descriptorList) {
    	if (descriptorList == null || descriptorList.length == 0)
    		return null;

    	int descriptorSize = descriptorList[0].length;
    	int rowCount = descriptorList.length;

    	long[][] invertedDescriptors = new long[descriptorSize*64][(rowCount+63)/64];


    	return invertedDescriptors;
		}*/
	}
