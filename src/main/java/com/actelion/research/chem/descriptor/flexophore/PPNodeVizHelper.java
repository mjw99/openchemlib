package com.actelion.research.chem.descriptor.flexophore;

import com.actelion.research.chem.Molecule3D;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.descriptor.flexophore.redgraph.SubGraphIndices;

import java.util.ArrayList;
import java.util.List;

/*
 

 
 Created by Modest von Korff 
 16/02/2024
 
 */
public class PPNodeVizHelper {

    public static PPNodeViz createWithoutCoordinates(SubGraphIndices sgi, int indexPPPoint, Molecule3D mol){
        int [] arrIndexAtomFrag = sgi.getAtomIndices();
        PPNodeViz ppNodeViz = new PPNodeViz();
        ppNodeViz.setIndex(indexPPPoint);
        for (int index : arrIndexAtomFrag) {
            int interactionType = mol.getInteractionAtomType(index);
            ppNodeViz.add(interactionType);
            ppNodeViz.addIndexOriginalAtom(index);
        }
        ppNodeViz.realize();
        return ppNodeViz;
    }
    public static List<PPNodeViz> createWithoutCoordinates(List<SubGraphIndices> liSubGraphIndices, Molecule3D mol){
        List<PPNodeViz> liPPNodeViz = new ArrayList<>();
        for (int i = 0; i < liSubGraphIndices.size(); i++) {
            SubGraphIndices sgi = liSubGraphIndices.get(i);
            PPNodeViz ppNodeViz = createWithoutCoordinates(sgi, i, mol);
            liPPNodeViz.add(ppNodeViz);
        }
        return liPPNodeViz;
    }

}
