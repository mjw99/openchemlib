package com.actelion.research.chem.phesa.pharmacophore.pp;

import com.actelion.research.chem.Coordinates;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.conf.Conformer;
import com.actelion.research.chem.phesa.pharmacophore.PharmacophoreCalculator;

import java.util.ArrayList;
import java.util.List;

public class AliphaticPoint implements IPharmacophorePoint {
	private int referenceAtom;
	private List<Integer> atoms;
	private Coordinates center;
	private Coordinates directionality = new Coordinates(0.0,0.0,0.0);

	public AliphaticPoint(StereoMolecule mol, int a, List<Integer> atoms) {
		referenceAtom = a;
		this.atoms = atoms;
		directionality = new Coordinates(0.0,0.0,0.0);
		updateCoordinates(mol);
	}

	public AliphaticPoint(AliphaticPoint aP) {
		referenceAtom = aP.referenceAtom;
		directionality = new Coordinates(aP.directionality);
		center = new Coordinates(aP.center);
		atoms = new ArrayList<Integer>();
		directionality = new Coordinates(0.0,0.0,0.0);
		for(int ringAtom : aP.atoms) {
			atoms.add(ringAtom);
		}
	}

	@Override
	public Coordinates getCenter() {
		return center;
	}

	@Override
	public void updateCoordinates(Coordinates[] coords) {

	}


	public void updateCoordinates(StereoMolecule mol) {
		Coordinates com = new Coordinates(0,0,0);
		for(int atom : atoms) {
			com.add(mol.getCoordinates(atom));
		}
		com.scale(1.0/( atoms.size()));

		center = com;
	}


	public void updateCoordinates(Conformer conf) {
		Coordinates com = new Coordinates(0,0,0);
		for(int atom : atoms) {
			com.add(conf.getCoordinates(atom));
		}
		com.scale(1.0/( atoms.size()));

		center = com;
	}


	@Override
	public Coordinates getDirectionality() {
		return directionality;
	}

	@Override
	public Coordinates getRotatedDirectionality(double[][] m) {
		return null;
	}

	private AliphaticPoint(String ppString, StereoMolecule mol) {
		decode(ppString,mol);
	}
	
	public static AliphaticPoint fromString(String ppString, StereoMolecule mol) {
		return new AliphaticPoint(ppString,mol);
	}
	

	private void decode(String ppString, StereoMolecule mol) {
		String[] strings = ppString.split(" ");
		referenceAtom = Integer.decode(strings[1]);
		atoms = new ArrayList<Integer>();
		for(int i=2;i<strings.length;i++) {
			atoms.add(Integer.decode(strings[i]));
		}
		updateCoordinates(mol);
	}

	@Override
	public String encode() {
		StringBuilder molVolString = new StringBuilder();
		molVolString.append("r");
		molVolString.append(" ");
		molVolString.append(Integer.toString(referenceAtom));
		molVolString.append(" ");
		//molVolString.append(Integer.toString(neighbours.size()));
		//molVolString.append(" ");
		for(Integer ringAtom : atoms) {
			molVolString.append(ringAtom);
			molVolString.append(" ");
		}
		return molVolString.toString().trim();
	}

	@Override
	public double getSimilarity(IPharmacophorePoint pp) {
		double result = 0.0;
		if(pp instanceof AliphaticPoint) {
			result = 1.0;
		}
		return result;
	}

	@Override
	public int getCenterID() {
		return referenceAtom;
	}

	@Override
	public void setCenterID(int id) {

	}

	@Override
	public void setDirectionality(Coordinates directionality) {
		return;

	}
	

	@Override
	public void updateAtomIndeces(int[] map) {
		referenceAtom = map[referenceAtom];
		for(int i=0;i<atoms.size();i++) {
			int neighbour = map[atoms.get(i)];
			atoms.set(i, neighbour);
		}

		
	}

	@Override
	public IPharmacophorePoint copyPharmacophorePoint() {
		// TODO Auto-generated method stub
		return new AliphaticPoint(this);
	}

	@Override
	public void getDirectionalityDerivativeCartesian(double[] grad, double[] v, Coordinates di, double sim) {
		return; //no directionality 
		
	}
	
	@Override 
	
	public double getVectorSimilarity(IPharmacophorePoint pp2,Coordinates directionalityMod) {
		return 1.0;
	}
		
	@Override
	 public double getVectorSimilarity(IPharmacophorePoint pp2) {
		return 1.0;
	}
	
	@Override
	public int getFunctionalityIndex() {
		return PharmacophoreCalculator.LIPO_ID;
	}

	public List<Integer> getAtoms() {
		return atoms;
	}
}
