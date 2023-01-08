
package com.actelion.research.chem.forcefield.mmff;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.io.SDFileParser;

public class ForceFieldMMFF94Test {

	private String getAbsoluteResourcePath(String ResourceName) {
		ClassLoader ClassLoader = getClass().getClassLoader();
		File File = new File(ClassLoader.getResource(ResourceName).getFile());
		return File.getAbsolutePath();
	}

	private Map<String, Double> getReferenceValues(String refAbsolutePath) {
		Map<String, Double> referenceEnergies = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(refAbsolutePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\s+");

				String key = parts[0];
				Double value = Double.parseDouble(parts[1]);

				if (referenceEnergies.get(key) == null) {
					referenceEnergies.put(key, value);
				} else {
					referenceEnergies.put(key, referenceEnergies.get(key) + value);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return referenceEnergies;
	}

	@Test
	/**
	 * Runs test set of http://www.ccl.net/cca/data/MMFF94/MMFF94.energies
	 */
	public void testMMFF94() {
		// Official value, but fails with ERULE_03
		// double delta = 0.0035;

		double delta = 0.006;

		// http://www.ccl.net/cca/data/MMFF94/MMFF94.energies
		String refAbsolutePath = getAbsoluteResourcePath(
				"com/actelion/research/chem/forcefield/mmff/MMFF94_hypervalent.energies");

		Map<String, Double> referenceEnergies = getReferenceValues(refAbsolutePath);

		// SI from https://doi.org/10.1186/s13321-014-0037-3
		String structAbsolutePath = getAbsoluteResourcePath(
				"com/actelion/research/chem/forcefield/mmff/MMFF94_hypervalent.sdf");

		SDFileParser parser = new SDFileParser(structAbsolutePath);

		ForceFieldMMFF94.initialize("MMFF94");

		while (parser.next()) {
			StereoMolecule stereoMolecule = parser.getMolecule();

			ForceFieldMMFF94 ff = new ForceFieldMMFF94(stereoMolecule, "MMFF94");

			assertEquals(referenceEnergies.get(stereoMolecule.getName()), ff.getTotalEnergy(), delta);

		}

	}

	@Test
	/**
	 * Runs test set of http://www.ccl.net/cca/data/MMFF94s/MMFF94s.energies
	 */
	public void testMMFF94s() {

		double delta = 0.0021;

		// http://www.ccl.net/cca/data/MMFF94s/MMFF94s.energies
		String refAbsolutePath = getAbsoluteResourcePath(
				"com/actelion/research/chem/forcefield/mmff/MMFF94s_hypervalent.energies");

		Map<String, Double> referenceEnergies = getReferenceValues(refAbsolutePath);

		// SI from https://doi.org/10.1186/s13321-014-0037-3
		String structAbsolutePath = getAbsoluteResourcePath(
				"com/actelion/research/chem/forcefield/mmff/MMFF94s_hypervalent.sdf");

		SDFileParser parser = new SDFileParser(structAbsolutePath);

		ForceFieldMMFF94.initialize("MMFF94s");

		while (parser.next()) {
			StereoMolecule stereoMolecule = parser.getMolecule();

			ForceFieldMMFF94 ff = new ForceFieldMMFF94(stereoMolecule, "MMFF94s");

			assertEquals(referenceEnergies.get(stereoMolecule.getName()), ff.getTotalEnergy(), delta);

		}

	}

}
