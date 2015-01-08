import static java.lang.Math.PI;

import java.io.PrintStream;


/**
 * Luokka, jossa on erilaisten kuvioiden piirtämiseen matriisien avulla liittyviä staattisia metodeja.
 *
 * @author Sampo Osmonen
 */
public class Star {

	/**
	 * Piirrä tähti haluttuun taulukkoon merkitsemällä tähden sisällä olevat taulukon paikat arvolla 1.
	 *
	 * @param x Tähden keskipisteen haluttu "sarakeindeksi" kohdematriisissa.
	 * @param y Tähden keskipisteen haluttu "rivi-indeksi" kohdematriisissa.
	 * @param direction Tähden ensimmäisen sakaran suuntakulma radiaaneina. 0-kulma osoittaa matriisiin nähden suoraan oikealle, ja kulma kasvaa vastapäivään.
	 * @param sides Tähden sakaroiden määrä.
	 * @param size Tähden sakaran korkeus.
	 * @param canvas Kohdematriisi, johon tähti piirretään. Tähden sisällä olevat matriisin paikat merkitään arvolla 1.
	 */
	public static void drawStar(int x, int y, double direction, int sides, double size, int[][] canvas) {
		double polySize = size / 2.0;  // Monikulmion "minimisäde".
		// Piirrä monikulmio tähden keskustaksi.
		// Pyöristysvirheet ja iso pistekoko voi aiheuttaa välejä sakarakolmioiden ja keskustan välille. Keskustan kokoa voi kasvattaa noin kertoimella 1.05 tämän välttämiseksi, tai vaihtoehtoisesti muuttaa sakaroiden kokoa.
		drawPolygon(x, y, direction, sides, polySize, canvas);

		// Kulma, johon ensimmäinen sakara osoittaa.
		double dir = direction;
		double angle = 2*PI / sides;
		double halfAngle = PI / sides;
		double width = 2 * (Math.tan(halfAngle) * polySize);  // Tähden sakaran muodostavan kolmion leveys, sama kuin monikulmion sivun pituus.
		double dist = size + polySize;  // Sakara-kolmion kärjen etäisyys monikulmion sivusta.
		// Kolmion kärjen koordinaatit.
		int startX = x + (int)(Math.cos(dir) * dist);
		int startY = y - (int)(Math.sin(dir) * dist);

		// Piirrä monikulmiota vastaava määrä sarakekolmioita.
		for (int i = 0; i < sides; i++) {
			// Pyöristysvirheet ja iso pistekoko voi aiheuttaa välejä sakarakolmioiden ja keskustan välille. Sakaran kokoa voi kasvattaa noin kertoimella 1.025 tämän välttämiseksi, tai vaihtoehtoisesti muuttaa keskusmonikulmion kokoa.
			drawTriangle(startX, startY, dir+PI, width, size*1.025, canvas);
			dir += angle;
			startX = x + (int)(Math.cos(dir) * dist);
			startY = y - (int)(Math.sin(dir) * dist);
		}
	}

	/**
	 * Piirrä monikulmio haluttuun taulukkoon merkitsemällä monikulmion sisällä olevat taulukon paikat arvolla 1.
	 *
	 * @param x Monikulmion keskipisteen keskipisteen haluttu "sarakeindeksi" kohdematriisissa.
	 * @param y Monikulmion keskipisteen keskipisteen haluttu "rivi-indeksi" kohdematriisissa.
	 * @param direction Monikulmion ensimmäisen sivun suuntakulma radiaaneina. 0-kulma osoittaa matriisiin nähden suoraan oikealle, ja kulma kasvaa vastapäivään.
	 * @param sides Monikulmion sivujen määrä.
	 * @param size Monikulmion "minimisäteen" pituus, eli sivun minimietäisyys keskipisteestä.
	 * @param canvas Kohdematriisi, johon monikulmio piirretään. Monikulmion sisällä olevat matriisin paikat merkitään arvolla 1.
	 */
	public static void drawPolygon(int x, int y, double direction, int sides, double size, int[][] canvas) {
		// Kulma, johon ensimmäinen sivu osoittaa.
		double dir = direction;
		double angle = 2*PI / sides;
		double halfAngle = PI / sides;
		double width = 2 * (Math.tan(halfAngle) * size);
		// Piirrä monikulmio kolmioista.
		for (int i = 0; i < sides; i++) {
			drawTriangle(x, y, dir, width, size, canvas);
			dir += angle;
		}
	}

	/**
	 * Luo taulukko, jossa on koordinaatiston 1. neljännekseen piirretty tasakylkinen kolmio.
	 *
	 * @param direction Kulma, jossa kolmion kanta on lähtöpisteeseen nähden. Radiaaneina, oltava välillä [0, PI/2]. 0-kulma osoittaa matriisiin nähden suoraan oikealle, ja kulma kasvaa vastapäivään.
	 * @param width Kolmion kanta.
	 * @param height Kolmion korkeus.
	 * @return Matriisi, johon on piirretty tasakylkinen kolmio. Kolmio luodaan merkitsemällä kolmion sisällä olevat taulukon paikat arvolla 1, tyhjät paikat on merkitty arvolla 0. Kolmion kantaa vastakkainen kulma piirretään taulukon keskikohtaan.
	 */
	public static int[][] drawTriangleToFirstQuarter(double direction, double width, double height) {
		if (direction > PI/2 || direction < 0.0) {
			throw new IllegalArgumentException("Vain kulmat välillä [0, PI/2] sallitaan (annettu kulma: " + direction +")");
		}

		double halfPi = PI / 2;

		double halfWidth = width / 2.0;
		double halfAngle = Math.atan(halfWidth/height);
		double dirPlus = direction + halfAngle;  // Suuremmassa kulmassa olevan sivun kulma.
		double dirMinus = direction - halfAngle;  // Pienemmässä kulmassa olevan sivun kulma.
		double sideLength = height / Math.cos(halfAngle);
		double xBound = Math.cos(dirPlus) * sideLength;  // x-akselin kohta, jossa x:n arvoa vastaava suurin mahdollinen kolmion sisällä oleva y:n arvo alkaa laskea nousemisen sijaan. Siis kolmion ylimmän pisteen x-arvo.
		double yMax = Math.sin(dirPlus) * sideLength;  // y:n arvo x:n ollessa yhtäsuuri kuin xBound, siis suurin kolmion saama y-arvo.
		double yBound = Math.sin(dirMinus) * sideLength;  // y:tä vastaavan suurimman mahdollisen x-arvon käännekohta, vrt. xBound.
		double xMax = Math.cos(dirMinus) * sideLength;  // x:n arvo y:n ollessa yhtäsuuri kuin yBound, vrt. yMax.
		double helperAngle = halfPi - direction;  // Kun x / y-koordinaatti ylittää xBoundin / yBoundin, lasketaan vastakkaisen koordinaatiston suurin arvo eri tavalla. Apuna käytetään varsinaisen kolmion sisällä olevaa pienempää suorakulmaista kolmiota, jonka hypotenuusa on varsinaisen kolmion hypotenuusalla, ja jonka toinen tunnettu sivu on x / y-koordinaatin etäisyys xBoundista / yBoundista. Kyseinen kulma on pikkukolmion "alempi" kulma joka ei ole suora, ja tämä arvo on kulman koko.

		double sizeD = Math.max(sideLength*2, height*2);
		int size = (int)(sizeD+1);  // Kolmion vaatiman taulukon koko.
		if (size % 2 == 0) {
			size++;
		}
		int[][] canvas = new int[size][size];  // Taulukko, johon kolmio sijoitetaan.

		// Koordinaatiston origo keskelle taulukkoa.
		double xOrigo = size / 2;
		double yOrigo = size / 2;

		for (int i = 0; i < canvas.length; i++) {
			for (int j = 0; j < canvas[0].length; j++) {
				// Suhteutetaan taulukon indeksi koordinaatistoon.
				double x = j - xOrigo;
				double y = yOrigo - i;

				// Pisteitä, jotka sijoittuvat 3. neljännekseen ei tarvitse laskea, koska mikään osa kolmiosta ei tule sijoittumaan niihin. Tämä välttää ylimääräisten tarkistusten lisäämien muihin ehtoihin siltä varalta, että kolmion kulma on yli PI/2.
				if (x < 0 && y < 0) {
					//System.out.print("  ");  // DEBUG
					continue;
				}

				double yMaxDist;  // y:n suurin sallittu arvo x:n arvoon nähden.
				double xMaxDist;  // x:n suurin sallittu arvo y:n arvoon nähden.
				if (x >= xBound - 0.001) {
					// Lasketaan y:n suurin sallittu arvo yBoundin jälkeen "apukolmion" ja käännekohdan y-arvon avulla.
					yMaxDist = yMax - (Math.tan(helperAngle) * (x - xBound));
				}
				else {
					// y:n suurin sallittu arvo sivun ja x-koordinaatin avulla.
					yMaxDist = Math.tan(dirPlus) * x;
				}

				if (y >= yBound - 0.001) {
					// Lasketaan x:n suurin sallittu arvo yBoundin jälkeen "apukolmion" ja käännekohdan x-arvon avulla.
					xMaxDist = xMax - ((y - yBound) / Math.tan(helperAngle));
				}
				else {
					// x:n suurin sallittu arvo sivun ja y-koordinaatin avulla.
					xMaxDist = y / Math.tan(dirMinus);
				}


				double yMinDist;  // y:n pienin sallittu arvo x:n arvoon nähden.
				double xMinDist;  // x:n pienin sallittu arvo y:n arvoon nähden.
				// Jos kolmion suuremman kulman omaava sivu menee y-akselin yli, niin pienimmän sallitun y-arvon laskua on tarkistettava, tällä y-arvolla on nimittäin käännekohta y-akselin kohdalla.
				if (dirPlus > halfPi && x < 0) {
					yMinDist = Math.tan(dirPlus) * -x;
				}
				else {
					yMinDist = Math.tan(dirMinus) * x;
				}

				// Jos kolmion pienemmän kulman omaava sivu menee x-akselin yli, niin pienimmän sallitun x-arvon laskua on tarkistettava, tällä x-arvolla on nimittäin käännekohta x-akselin kohdalla.
				if (dirMinus < 0 && y < 0)  {
					xMinDist = -y / Math.tan(dirMinus);
				}
				else {
					xMinDist = y / Math.tan(dirPlus);
				}

				// Jos nykyinen taulukon kohta on koordinaatistoon muunnettuna sekä x:n että y:n ala- ja ylärajojen sisällä, merkitään kohta.
				if (x >= xMinDist-0.01 && x <= xMaxDist+0.01 && y >= yMinDist-0.01 && y <= yMaxDist+0.01) {  // TODO ei kauheen siisti
					canvas[i][j] = 1;
					// System.out.print(" *");  // DEBUG
				}
				//else System.out.print(" .");  // DEBUG
			}
			//System.out.println("");  // DEBUG
		}
		return canvas;
	}

	/**
	 * Piirrä tasakylkinen kolmio haluttuun taulukkoon merkitsemällä kolmion sisällä olevat taulukon paikat arvolla 1.
	 *
	 * @param x Kolmion kantaa vastakkaisen kulman keskipisteen keskipisteen haluttu "sarakeindeksi" kohdematriisissa.
	 * @param y Kolmion kantaa vastakkaisen kulman keskipisteen keskipisteen haluttu "rivi.indeksi" kohdematriisissa.
	 * @param direction Kolmion kantaa vastakkaisen kulman haluttu suuntakulma radiaaneina. 0-kulma osoittaa matriisiin nähden suoraan oikealle, ja kulma kasvaa vastapäivään.
	 * @param width Kolmion haluttu kannan pituus.
	 * @param height Kolmion haluttu korkeus.
	 * @param canvas Kohdematriisi, johon kolmio piirretään. Kolmion sisällä olevat matriisin paikat merkitään arvolla 1.
	 */
	public static void drawTriangle(int x, int y, double direction, double width, double height, int[][] canvas) {
		// Muunnetaan piirrettävän kolmion suuntakulma välille [0, PI/2]. Tämän jälkeen haluttu kolmio saadaan peilaamalla 1. neljännekseen piirrettyä kolmiota tarpeen mukaan x- tai y-akselin tai molempien suhteen.
		boolean invertX = false;
		boolean invertY = false;
		double direction2 = direction;

//		if (direction2 < 0) {
//			direction2 = -direction2;
//			invertY = true;
//		}
		// Poistetaan ylimääräiset täysikulmat ja muunnetaan mahdollinen negatiivinen kulma vastaaavaksi positiiviseksi.
		direction2 = mod(direction2, 2*PI);

		// Kolmio on 2. tai 3. neljänneksessä.
		if (Math.cos(direction2) < 0) {
			// Peilataan kulma y-akselin oikealle puolelle.
			direction2 = PI - direction2;
			invertX = !invertX;
		}
		// Kolmio on 3. tai 4. neljänneksessä.
		if (Math.sin(direction2) < 0) {
			// Peilataan kulma x-akselin yläpuolelle.
			// Toimii, koska "direction2" on nyt positiivinen, ja negatointi heijastaa sen halutusti x-akselin yläpuolelle. Modulo-operaatio antaa tälle saadulle negatiiviselle kulmalle halutun positiivisen vastinkulman.
			direction2 = -direction2;
			direction2 = mod(direction2, 2*PI);
			invertY = !invertY;
		}
		int[][] triangle = drawTriangleToFirstQuarter(direction2, width, height);
		if (invertX) {
			invertX(triangle);
		}
		if (invertY) {
			invertY(triangle);
		}
		// Yhdistä nyt oikeassa asennossa oleva kolmion matriisi haluttuun taustamatriisiin.
		mask(canvas, triangle, x, y);
	}

	/**
	 * Peilaa matriisin sarakeindeksien arvot.
	 * @param matrix Operoitava matriisi.
	 * @return Annettu matriisi operaation jälkeen.
	 * @example
	 * <pre name="test">
	 *   #import java.util.Arrays;
	 *   int[][] m1 = {
	 *                  {  5,-1,21, 8 },
	 *                  {  2, 1, 1, 2 },
	 *                  {  2, 1, 6, 3 },
	 *                  {  0, 3, 0, 4 },
	 *                };
	 *   int[][] odotus = {
	 *                      {  8,21,-1, 5 },
	 *                      {  2, 1, 1, 2 },
	 *                      {  3, 6, 1, 2 },
	 *                      {  4, 0, 3, 0 },
	 *                    };
	 *    ;
	 *    Star.invertX(m1);
	 *    Arrays.deepEquals(odotus, m1) === true;
	 *    Star.invertX(odotus);
	 *    Star.invertX(m1);
	 *    Arrays.deepEquals(odotus, m1) === true;
	 * </pre>
	 */
	public static int[][] invertX(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length/2; j++) {
				int temp = matrix[i][j];
				matrix[i][j] = matrix[i][matrix[0].length-1 - j];
				matrix[i][matrix[0].length-1 - j] = temp;
			}
		}
		return matrix;
	}

	/**
	 * Peilaa matriisin rivi-indeksien arvot.
	 * @param matrix Operoitava matriisi.
	 * @return Annettu matriisi operaation jälkeen.	 * @example
	 * <pre name="test">
	 *   #import java.util.Arrays;
	 *   int[][] m1 = {
	 *                  {  5,-1,21, 8 },
	 *                  {  2, 1, 1, 2 },
	 *                  {  2, 1, 6, 3 },
	 *                  {  0, 3, 0, 4 },
	 *                };
	 *   int[][] odotus = {
	 *                      {  0, 3, 0, 4 },
	 *                      {  2, 1, 6, 3 },
	 *                      {  2, 1, 1, 2 },
	 *                      {  5,-1,21, 8 },
	 *                    };
	 *    ;
	 *    Star.invertY(m1);
	 *    Arrays.deepEquals(odotus, m1) === true;
	 *    Star.invertY(odotus);
	 *    Star.invertY(m1);
	 *    Arrays.deepEquals(odotus, m1) === true;
	 * </pre>
	 */
	public static int[][] invertY(int[][] matrix) {
		for (int i = 0; i < matrix.length/2; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				int temp = matrix[i][j];
				matrix[i][j] = matrix[matrix.length-1 - i][j];
				matrix[matrix.length-1 - i][j] = temp;
			}
		}
		return matrix;
	}

	/**
	 * Yhdistä emomatriisi ja toinen matriisi.
	 *
	 * @param array Emomatriisi.
	 * @param mask Maskimatriisi.
	 * @param x Emomatriisin sarakeindeksi, johon maskimatriisin keskipiste sijoitetaan.
	 * @param y Emomatriisin rivi-indeksi, johon maskimatriisin keskipiste sijoitetaan.
	 * @return Emomatriisi maskattuna.
	 * @example
	 * <pre name="test">
	 *   #import java.util.Arrays;
	 *   int[][] alkukuva = {
	 *                   {  0, 0, 1, 0 },
	 *                   {  1, 1, 1, 1 },
	 *                   {  0, 1, 0, 0 },
	 *                   {  0, 0, 0, 0 },
	 *                 };
	 *   int[][] kuva = Star.cloneMatrix(alkukuva);
	 *   int[][] maski = {
	 *                    { 0, 1, 1 },
	 *                    { 0, 1, 0 },
	 *                    { 1, 0, 1 },
	 *                  };
	 *    ;
	 *    int[][] odotus;
	 *    odotus = new int[][] {
	 *               {  1, 0, 1, 0 },
	 *               {  1, 1, 1, 1 },
	 *               {  0, 1, 0, 0 },
	 *               {  0, 0, 0, 0 },
	 *             };
	 *    Star.mask(kuva, maski, 0, 0);
	 *    Arrays.deepEquals(odotus, kuva) === true;
	 *    kuva = Star.cloneMatrix(alkukuva);
	 *    odotus = new int[][] {
	 *               {  0, 0, 1, 0 },
	 *               {  1, 1, 1, 1 },
	 *               {  0, 1, 0, 1 },
	 *               {  0, 0, 0, 1 },
	 *             };
	 *    Star.mask(kuva, maski, 3, 3);
	 *    Arrays.deepEquals(odotus, kuva) === true;
	 *    kuva = Star.cloneMatrix(alkukuva);
	 *    odotus = new int[][] {
	 *               {  0, 0, 1, 0 },
	 *               {  1, 1, 1, 1 },
	 *               {  0, 1, 1, 0 },
	 *               {  0, 1, 0, 1 },
	 *             };
	 *    Star.mask(kuva, maski, 2, 2);
	 *    Arrays.deepEquals(odotus, kuva) === true;
	 * </pre>
	 */
	public static int[][] mask(int[][] array, int[][] mask, int x, int y) {
		int yBorder = mask.length / 2;  // Maskimatriisin keskustan toisella puolella pystytasossa olevien paikkojen määrä, "padding".
		int xBorder = mask[0].length / 2;  // Maskimatriisin keskustan toisella puolella vaakatasossa olevien paikkojen määrä, "padding".
		int matrixStartY = Math.max(0, yBorder - y);  // Maskin 1. "rivi-indeksi", josta iterointi voidaan aloittaa kun huomioidaan ettei päällekkäisyys välttämättä ole täydellistä.
		int matrixStartX = Math.max(0, xBorder - x);  // Maskin 1. "sarakeindeksi", josta iterointi voidaan aloittaa kun huomioidaan ettei päällekkäisyys välttämättä ole täydellistä.
		int my = matrixStartY;  // Maskin "rivi".
		int mx = matrixStartX;  // Maskin "sarake".
		int arrStartY = Math.max(0, y - yBorder);  // Emomatriisin "rivin" alkuindeksi, vrt matrixStartY.
		int arrStartX = Math.max(0, x - xBorder);  // Emomatriisin "sarakkeiden" alkuindeksi, vrt matrixStartX.
		int arrEndY = Math.min(array.length - 1, y + yBorder);  // Emomatriisin "rivin" loppuindeksi, vrt arrStartY.
		int arrEndX = Math.min(array[0].length - 1, x + xBorder);  // Emomatriisin "sarakkeiden" loppuindeksi, vrt arrStartX.
		for (int i = arrStartY; i <= arrEndY; i++) {
			for (int j = arrStartX; j <= arrEndX; j++) {
				// Operoidaan vain, jos emomatriisin arvo on 0.
				array[i][j] |= mask[my][mx];
				mx++;
			}
			// Resetoidaan "sarakeindeksi" rivin vaihtuessa.
			mx = matrixStartX;
			my++;
		}
		return array;
	}

	/**
	 * Modulo-operaatio, "mikä on minimijakojäännös kun a jaetaan b:llä".
	 * @param a Jaettava.
	 * @param b Jakaja.
	 * @return Jaettavan ja jakajan pienin mahdollinen positiivinen jakojäännös (jakajan ollessa nolla tai suurempi).
	 * @example
	 * <pre name="test">
	 *   #TOLERANCE=0.01;
	 *   Star.mod(5, 2) ~~~ 1;
	 *   Star.mod(2, 5) ~~~ 2;
	 *   Star.mod(9, 3) ~~~ 0;
	 *   Star.mod(0, 4) ~~~ 0;
	 *   Star.mod(-7, 3) ~~~ 2;
	 *   Star.mod(-3, 7) ~~~ 4;
	 *   Star.mod(7, -3) ~~~ -2;
	 *   Star.mod(3, -7) ~~~ -4;
	 *   Star.mod(-7, -3) ~~~ -1;
	 *   Star.mod(-3, -7) ~~~ -3;
	 *   Star.mod(5, 2.3) ~~~ 0.4;
	 *   Star.mod(2.3, 5) ~~~ 2.3;
	 *   Star.mod(9.1, 2.8) ~~~ 0.7;
	 *   Star.mod(0.5, 4.5) ~~~ 0.5;
	 *   Star.mod(-7.9, 3) ~~~ 1.1;
	 *   Star.mod(-3.9, 7) ~~~ 3.1;
	 *   Star.mod(7.9, -3) ~~~ -1.1;
	 *   Star.mod(3.9, -7) ~~~ -3.1;
	 *   Star.mod(-7.9, -3) ~~~ -1.9;
	 *   Star.mod(-3.9, -7) ~~~ -3.9;
	 * </pre>
	 */
	public static double mod(double a, double b) {
		int times = (int)Math.floor(a/b);
		return a - b * times;
	}

	/**
	 * Luo syväkopio kokonaislukumatriisista.
	 *
	 * @param matrix Kopioitava matriisi.
	 * @return Matriisin syväkopio.
	 * @example
	 * <pre name="test">
	 *   #import java.util.Arrays;
	 *   int[][] m1 = {
	 *                  {  5,-1,21, 8 },
	 *                  {  2, 1, 1, 2 },
	 *                  {  2, 1, 6, 3 },
	 *                  {  0, 3, 0, 4 },
	 *                };
	 *   int[][] m2 = Star.cloneMatrix(m1);
	 *   m2 != m1 === true;
	 *   Arrays.deepEquals(m1, m2) === true;
	 * </pre>
	 */
	public static int[][] cloneMatrix(int[][] matrix) {
		if (matrix == null) {
			return null;
		}
		int[][] result = new int[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			result[i] = matrix[i].clone();
		}
		return result;
	}

	/**
	 * Tulosta (kuva)matriisi haluttuun syötevirtaan haluttuja merkkejä käyttäen. Matriisin sarakkeet erotetaan ylimääräisillä välilyönneillä mittasuhteiden parantamiseksi.
	 *
	 * @param canvas Tulostettava matriisi.
	 * @param mark Merkki, joka tulostetaan jokaisen matriisin nollasta eroavan paikan kohdalle.
	 * @param out Syötevirta, johon tulostetaan.
	 * @example
	 * <pre name="test">
	 *   #import java.io.*;
	 *   String nl = System.lineSeparator();
	 *   ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 *   PrintStream ps = new PrintStream(bos);
	 *   int[][] m1 = {
	 *                  {  5,-1,21, 0 },
	 *                  {  2, 0, 1, 2 },
	 *                  {  2, 1, 0, 3 },
	 *                  {  0, 3, 0, 4 },
	 *                };
	 *   Star.printCanvas(m1, '*', ps);
	 *   String odotus = " * * *  " + nl +
	 *                   " *   * *" + nl +
	 *                   " * *   *" + nl +
	 *                   "   *   *";
	 *   odotus.equals(bos.toString());
	 * </pre>
	 */
	public static void printCanvas(int[][] canvas, char mark, PrintStream out) {
		String mark2 = String.valueOf(mark);
		StringBuilder sb = new StringBuilder("");
		for (int[] m : canvas) {
			for (int i : m) {
				if (i == 0) {
					sb.append("  ");
				}
				else {
					sb.append(" " + mark2);
				}
			}
			out.println(sb);
			sb = new StringBuilder("");
		}
	}

	/**
	 * Tulosta (kuva)matriisi konsoliin haluttuja merkkejä käyttäen. Matriisin sarakkeet erotetaan ylimääräisillä välilyönneillä mittasuhteiden parantamiseksi.
	 *
	 * @param canvas Tulostettava matriisi.
	 * @param mark Merkki, joka tulostetaan jokaisen matriisin nollasta eroavan paikan kohdalle.
	 */
	public static void printCanvas(int[][] canvas, char mark) {
		printCanvas(canvas, mark, System.out);
	}

	/**
	 * Manuaalinen testisekvenssi piirtoalohjelmien ja integraation testaamiseksi.
	 *
	 * @param args Ei käytössä.
	 */
	public static void main(String[] args) {
		try {
			int[][] m;

			for (double dir = -3*PI; dir < 3*PI; dir += 0.1) {
				m = new int[50][50];
				drawTriangle(25, 25, dir, 10, 15, m);
				printCanvas(m, '*');
				Thread.sleep(100);
			}

			Thread.sleep(1000);

			for (double dir = 0.0; dir < 2*PI; dir += 0.1) {
				m = new int[50][50];
				drawTriangle(25, 25, dir, 45, 15, m);
				printCanvas(m, '+');
				Thread.sleep(100);
			}

			Thread.sleep(1000);

			m = new int[60][60];
			drawPolygon(25, 25, PI/2, 5, 15, m);
			printCanvas(m, '*');

			Thread.sleep(1000);

			m = new int[60][60];
			drawStar(30, 30, PI/2, 5, 15, m);
			printCanvas(m, '*');

			Thread.sleep(1000);

			m = new int[60][60];
			drawStar(30, 30, PI/2, 6, 20, m);
			printCanvas(m, '*');

			Thread.sleep(1000);

			m = new int[60][60];
			drawStar(0, 0, PI/2, 7, 20, m);
			drawStar(0, 59, PI/2, 7, 20, m);
			drawStar(59, 0, PI/2, 7, 20, m);
			drawStar(65, 65, PI/2, 7, 20, m);
			printCanvas(m, '*');

			Thread.sleep(1000);

			double direction = 0.0;
			for (int i = 0; i < 75; i++) {
				m = new int[60][100];
				drawStar(25, 25, direction, 5, 15, m);
				drawStar(60, 15, direction*2, 5, 6, m);
				drawStar(70, 45, -direction/2, 7, 10, m);
				direction += 0.1;
				printCanvas(m, '+');
				Thread.sleep(150);
			}

//			Thread.sleep(1000);
//
//			for (double dir = 0.0; dir < 2*PI; dir += 0.1) {
//				m = new int[50][50];
//				drawStar(25, 25, dir, 5, 15, m);
//				printCanvas(m, '*');
//				Thread.sleep(200);
//			}
//
//			Thread.sleep(1000);
//
//			m = new int[30][30];
//			drawStar(15, 15, PI/2, 5, 10, m);
//			printCanvas(m, '*');

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
