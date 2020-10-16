/** Copyright (c) 1997 - 2016* Actelion Pharmaceuticals Ltd.* Gewerbestrasse 16* CH-4123 Allschwil, Switzerland** All rights reserved.** Redistribution and use in source and binary forms, with or without* modification, are permitted provided that the following conditions are met:** 1. Redistributions of source code must retain the above copyright notice, this*    list of conditions and the following disclaimer.* 2. Redistributions in binary form must reproduce the above copyright notice,*    this list of conditions and the following disclaimer in the documentation*    and/or other materials provided with the distribution.* 3. Neither the name of the the copyright holder nor the*    names of its contributors may be used to endorse or promote products*    derived from this software without specific prior written permission.** THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR* ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.**/package com.actelion.research.chem;public class CanonizerUtil{	public enum IDCODE_TYPE	{		NORMAL, NOSTEREO, BACKBONE, TAUTOMER, NOSTEREO_TAUTOMER	}	/**	 * Generates an IDCODE for a defined and simplified state of a given molecule.	 * Optionally the simplification and idcode generation are done for the largest	 * unconnected fragment rather than considering all molecule fragments.	 * Allowed simplification types are:<br>	 * - NORMAL (with complete stereo information)<br>	 * - NOSTEREO (without stereo information)<br>	 * - TAUTOMER (generic tautomer: tautomer regions marked as [cBondQFSingle | cBondQFDouble] plus atom label encoding count of pi electrons, deuterium and tritium)<br>	 * - NOSTEREO_TAUTOMER (combines NOSTEREO and TAUTOMER)<br>	 * - BACKBONE (without stereo information; all bonds are changed to single bonds)<br>	 *	 * @param mol	source molecule to generate the IDCODE	 * @param type type of IDCODE requested	 * @param largestFragmentOnly	 * @return	 */	public static String getIDCode(StereoMolecule mol, IDCODE_TYPE type, boolean largestFragmentOnly)	{		switch (type) {			case NORMAL:				return getIDCode(mol, largestFragmentOnly);			case NOSTEREO:				return getIDCodeNoStereo(mol, largestFragmentOnly);			case BACKBONE:				return getIDCodeBackBone(mol, largestFragmentOnly);			case TAUTOMER:				return getIDCodeTautomer(mol, largestFragmentOnly);			case NOSTEREO_TAUTOMER:				return getIDCodeNoStereoTautomer(mol, largestFragmentOnly);			default:				break;		}		return null;	}	public static long getHash(StereoMolecule m, IDCODE_TYPE type, boolean largestFragmentOnly)	{		String code = getIDCode(m, type, largestFragmentOnly);		return code != null ? StrongHasher.hash(code) : 0;	}	public static long getNoStereoHash(StereoMolecule m, boolean largestFragmentOnly)	{		String code = getIDCodeNoStereo(m, largestFragmentOnly);		return code != null ? StrongHasher.hash(code) : 0;	}	public static long getTautomerHash(StereoMolecule m, boolean largestFragmentOnly)	{		String code = getIDCodeTautomer(m, largestFragmentOnly);		return code != null ? StrongHasher.hash(code) : 0;	}	public static long getNoStereoTautomerHash(StereoMolecule m, boolean largestFragmentOnly)	{		String code = getIDCodeNoStereoTautomer(m, largestFragmentOnly);		return code != null ? StrongHasher.hash(code) : 0;	}	public static long getBackboneHash(StereoMolecule m, boolean largestFragmentOnly)	{		String code = getIDCodeBackBone(m, largestFragmentOnly);		return code != null ? StrongHasher.hash(code) : 0;	}	private static String getIDCode(StereoMolecule mol, boolean largestFragmentOnly)	{		try {			if (!largestFragmentOnly)				return new Canonizer(mol).getIDCode();			mol = mol.getCompactCopy();			mol.stripSmallFragments(true);			new MoleculeNeutralizer().neutralizeChargedMolecule(mol);			return new Canonizer(mol).getIDCode();		} catch (Throwable e) {			System.err.println("WARN: getIDCode() " + e);			return null;		}	}	private static String getIDCodeNoStereo(StereoMolecule mol, boolean largestFragmentOnly)	{		try {			mol = mol.getCompactCopy();			if (largestFragmentOnly) {				mol.stripSmallFragments(true);				new MoleculeNeutralizer().neutralizeChargedMolecule(mol);			}			mol.stripStereoInformation();			return new SimpleCanonizer(mol).getIDCode();		} catch (Throwable e) {			System.err.println("WARN: getIDCodeNoStereo() " + e);			return null;		}	}	private static String getIDCodeTautomer(StereoMolecule mol, boolean largestFragmentOnly)	{		try {			if (largestFragmentOnly) {				mol = mol.getCompactCopy();				mol.stripSmallFragments(true);				new MoleculeNeutralizer().neutralizeChargedMolecule(mol);			}			StereoMolecule genericTautomer = new TautomerHelper(mol).createGenericTautomer();			return new Canonizer(genericTautomer, Canonizer.ENCODE_ATOM_CUSTOM_LABELS).getIDCode();		} catch (Throwable e) {			System.err.println("WARN: getIDCodeTautomer() " + e);			return null;		}	}	private static String getIDCodeNoStereoTautomer(StereoMolecule mol, boolean largestFragmentOnly)	{		try {			mol = mol.getCompactCopy();			if (largestFragmentOnly) {				mol.stripSmallFragments(true);				new MoleculeNeutralizer().neutralizeChargedMolecule(mol);			}			mol.stripStereoInformation();			StereoMolecule genericTautomer = new TautomerHelper(mol).createGenericTautomer();			return new Canonizer(genericTautomer, Canonizer.ENCODE_ATOM_CUSTOM_LABELS).getIDCode();		} catch (Throwable e) {			System.err.println("WARN: getIDCodeNoStereoTautomer() " + e);			return null;		}	}	private static String getIDCodeBackBone(StereoMolecule mol, boolean largestFragmentOnly)	{		try {			mol = mol.getCompactCopy();			if (largestFragmentOnly) {				mol.stripSmallFragments(true);				new MoleculeNeutralizer().neutralizeChargedMolecule(mol);			}			mol.stripStereoInformation();			int b = mol.getAllBonds();			for (int i = 0; i < b; i++)				mol.setBondType(i, Molecule.cBondTypeSingle);			return new SimpleCanonizer(mol).getIDCode();		} catch (Throwable e) {			System.err.println("WARN: getIDCodeBackBone() " + e);			return null;		}	}	/**	 * 64 bit hash, derived from numerical recipes	 * Will move this class later to dd_core.	 */	public static class StrongHasher	{		private static final long[] byteTable;		private static final long HSTART = 0xBB40E64DA205B064L;		private static final long HMULT = 7664345821815920749L;		static {			byteTable = new long[256];			long h = 0x544B2FBACAAF1684L;			for (int i = 0; i < 256; i++) {				for (int j = 0; j < 31; j++) {					h = (h >>> 7) ^ h;					h = (h << 11) ^ h;					h = (h >>> 10) ^ h;				}				byteTable[i] = h;			}		}		public static long hash(String cs) {			if (cs == null) return 1L;			long h = HSTART;			final long hmult = HMULT;			final long[] ht = byteTable;			for (int i = cs.length()-1; i >= 0; i--) {				char ch = cs.charAt(i);				h = (h * hmult) ^ ht[ch & 0xff];				h = (h * hmult) ^ ht[(ch >>> 8) & 0xff];			}			return h;		}	}}