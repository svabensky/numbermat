/*
    This file is part of Numbermat: Math Problem Generator.
    Copyright © 2014 Valdemar Svabensky

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package cz.muni.fi.Numbermat;

import cz.muni.fi.Numbermat.GUI.MainFrame;
import cz.muni.fi.Numbermat.GUI.Config;
import cz.muni.fi.Numbermat.GUI.UserInputChecker;
import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 * Methods for manipulating strings containing math, converting them to LaTeX
 * format and subsequently generating their image to be displayed in GUI or
 * TeX + PDF output on disk.
 * 
 * @author Valdemar Svabensky <395868(at)mail(dot)muni(dot)cz>
 */
public final class Utils {
    
    public static final String NEWLINE = System.lineSeparator();
  
    private Utils() {
        throw new IllegalStateException(this.getClass().getName() +
                " class should not be instantiated.");
    }
    
    /**
     * Replaces ASCII symbols commonly used in this program for their
     * LaTeX equivalents.
     * @param input Text
     * @return Text with "\cdot" instead of "*" and "\varphi" instead of "φ".
     */
    public static String prepareBasicMath(final String input) {
        String tmp = input.replaceAll("\\*", "\\\\cdot");
        return tmp.replaceAll("φ", "\\\\varphi");
    }
    
    /**
     * Changes ASCII math congruences symbols for their LaTeX equivalents.
     * @param input Text
     * @return Text with e.g. "\equiv" instead of "≡" or \pmod{n} instead of "(mod n)"
     */
    public static String prepareCongruencesMath(final String input) {
        String tmp = input.replaceAll(AlgorithmsSteps.CONG, " \\\\\\equiv ");
        tmp = tmp.replaceAll(AlgorithmsSteps.NOT_CONG, " \\\\not\\\\equiv ");
        tmp = tmp.replaceAll(AlgorithmsSteps.SEPARATOR, "\\\\hline ");
        tmp = tmp.replaceAll("   ", "\\\\quad ");
        if (tmp.contains("řešení")) {
            tmp = tmp.replaceAll("Neexistuje", "&\\\\text{Neexistuje");
            tmp = tmp.replaceAll("Existuje", "&\\\\text{Existuje");
            tmp = tmp.replaceAll("řešení.", "řešení.}");
        }
        
        final List<String> numbers = matchNumbers(input);
        for (int i = 0; i < numbers.size(); ++i) {
            final String n = numbers.get(i);
            tmp = tmp.replaceAll("\\(mod " + n + "\\)", "\\\\pmod{" + n + "}");
        }
        return tmp;
    }
    
    private static List<String> matchNumbers(final String input) {
        final List<String> numbers = new ArrayList<>();
        final Pattern p = Pattern.compile("[\\+-]?\\d+");
        final Matcher m = p.matcher(input); 
        while (m.find())
            numbers.add(m.group());
        return numbers;
    }
    
    /**
     * Searches for substrings of type "(a/b)" and replaces them with LaTeX fractions.
     * Can be called only after prepareCongruencesMath()!
     * @param input Text with substrings of type "(a/b)"
     * @return Text with "\frac{a}{b}" instead of "(a/b)"
     */
    public static String prepareFractionsMath(final String input) {
        if (!input.contains("/"))
            return input;
        final String fracBegin = "\\\\left(\\\\frac{";
        final String fracEnd = "}\\\\right)";
        
        String tmp = input;
        final List<String> numbers = matchNumbers(tmp);
        for (int i = 0; i < numbers.size(); i += 2) {
            if (i + 1 < numbers.size()) {
                final String n1 = numbers.get(i);
                final String n2 = numbers.get(i + 1);
                tmp = tmp.replaceAll("\\(" + n1 + "\\/" + n2 + "\\)",
                    fracBegin + n1 + "}{" + n2 + fracEnd);
            }
        }
        return tmp;
    }
    
    /**
     * Prepares LaTeX align environment.
     * @param input Text
     * @return String "\begin{align} Text \end{align}" with added & operators
     */
    public static String prepareAlignMath(final String input) {
        String tmp = input.replaceAll("=", "&=");
        tmp = tmp.replaceAll("\\\\equiv", "&\\\\equiv");
        tmp = tmp.replaceAll("\\\\not&\\\\equiv", "&\\\\not\\\\equiv");
        
        final StringBuilder sb = new StringBuilder(256);
        sb.append("\\begin{align}").append(NEWLINE).append(tmp);
        
        // Insert LaTeX newline symbol: "\\"  at the position of NEWLINE
        final int newLineReplacementBeginning = sb.lastIndexOf("{align}") + 9;
        int newlineIndex = sb.indexOf(NEWLINE, newLineReplacementBeginning);
        while (newlineIndex < sb.lastIndexOf(NEWLINE)) {
            sb.insert(newlineIndex, "\\\\");
            newlineIndex = sb.indexOf(NEWLINE, newlineIndex + 3);
        }
        
        sb.append("\\end{align}");
        return sb.toString();
    }
    
    /**
     * Prepares LaTeX alignat environment from align.
     * @param input Text
     * @return String "\begin{alignat} Text \end{alignat}" with added & operators
     */
    public static String prepareAlignatMath(final String input) {
        String tmp = input.replace("\\begin{align}", "\\begin{alignat}{2}");
        tmp = tmp.replace("\\end{align}", "\\end{alignat}");
        tmp = tmp.replaceAll("\\\\pmod", "&&\\\\pmod");
        tmp = tmp.replaceAll("÷", "\\\\div");
        
        final StringBuilder sb = new StringBuilder(256);
        try {
            final BufferedReader bufReader = new BufferedReader(new StringReader(tmp));
            String line;
            while ((line = bufReader.readLine()) != null) {
                if (line.split("&").length == 2)
                    line = line.replace("\\\\", " &&\\\\");
                sb.append(line).append(NEWLINE);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return sb.toString();
    }
    
    /**
     * Prepares LaTeX display math environment.
     * @param input Text
     * @return String "\[ Text \]"
     */
    public static String prepareDisplayMath(final String input) {
        String tmp = prepareBasicMath(input);
        tmp = replaceLast(tmp, NEWLINE, "");
        final StringBuilder sb = new StringBuilder(64);
        sb.append("\\[ ").append(tmp).append(" \\]").append(NEWLINE);
        return sb.toString();
    }
    
    /**
     * Prepares LaTeX matrix environment from ArrayList.toString().
     * @param input Text
     * @return String "\begin{pmatrix} Text \end{pmatrix}" with added & operators.
     */
    public static String prepareMatrix(final String input) {
        String tmp = input.replaceAll(",", " &");
        tmp = tmp.replaceAll("\\(", "").replaceAll("\\)", "");
        tmp = tmp.replaceAll(NEWLINE, "\\\\\\\\" + NEWLINE);
        
        final StringBuilder sb = new StringBuilder(64);
        sb.append("\\begin{pmatrix}").append(NEWLINE);
        sb.append(tmp).append(NEWLINE);
        sb.append("\\end{pmatrix}").append(NEWLINE);
        return sb.toString();
    }
    
    /**
     * Processes a LaTeX formula and typesets it into image.
     * The image can be rendered or saved with ImageIO.
     * This method uses JLaTeXMath library.
     * @param LaTeXInput Text formula in LaTeX syntax
     * @return Image containing typeset formula or null in case of error.
     */
    public static BufferedImage createLaTeXImage(final String LaTeXInput) {
        try {
            final TeXFormula formula = new TeXFormula(LaTeXInput);
            final TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
            icon.setInsets(new Insets(5, 5, 5, 5)); // Borders
            final int width = icon.getIconWidth();
            final int height = icon.getIconHeight();
            final BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            
            final Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, width, height);
            final JLabel jLabel = new JLabel();
            jLabel.setForeground(new Color(0, 0, 0));
            icon.paintIcon(jLabel, graphics, 0, 0);
            return image;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
    /**
     * Writes .tex file with mathematical problem and its solution on disk, then
     * runs pdfLaTeX on the written file and opens corresponding .pdf automatically.
     * This method uses Java LaTeX Report library.
     * @param pdfLaTeX Executable pdfLaTeX file or null for default TeX installation
     * @param workingDir Directory to write into
     * @param problem Mathematical problem in LaTeX syntax
     * @param solution Solution to the problem in LaTeX syntax
     * @param mainFrame Program GUI window for error messages
     */
    public static void exportPDF(final File pdfLaTeX, final File workingDir,
            final String problem, final String solution, final MainFrame mainFrame) {
        
        final java.util.Date date = new java.util.Date();
        final java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
        final String timestamp = ts.toString().replaceAll(":", "-");
        
        final StringBuilder fileName = new StringBuilder();
        fileName.append(workingDir.getAbsolutePath()).append(File.separator);
        fileName.append("priklad ").append(timestamp).append(".tex");
        final File texFile = new File(fileName.toString());
        writeTeXFile(texFile, problem, solution);
        
        final String errorMsgHeader = "Chyba při exportu do PDF";
        final String errorMsg = "Překlad PDFLaTeXem zlyhal.";
        try {
            final JLRGenerator pdfGenerator = new JLRGenerator();
            pdfGenerator.deleteTempFiles(false, true, true); // leave .tex file
            if (pdfLaTeX == null) {
                if (pdfGenerator.generate(texFile, workingDir, workingDir))
                    JLROpener.open(pdfGenerator.getPDF());
            } else {
                if (pdfGenerator.generate(pdfLaTeX, 1, texFile, workingDir, workingDir)) 
                    JLROpener.open(pdfGenerator.getPDF());
            }
            
            if (!pdfGenerator.getErrorMessage().equals("No errors occurred!")) {
                UserInputChecker.error(mainFrame, errorMsgHeader, errorMsg);
                System.err.println(pdfGenerator.getErrorMessage());
            }
        } catch (IOException ex) {
            UserInputChecker.error(mainFrame, errorMsgHeader, errorMsg);
            System.err.println(ex.getMessage());
        }
    }
    
    private static void writeTeXFile(final File file, final String problem, final String solution) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("\\documentclass[12pt,a4paper,oneside]{article}");
            writer.newLine();
            writer.write("\\usepackage[utf8]{inputenc}");
            writer.newLine();
            writer.write("\\usepackage[czech]{babel}");
            writer.newLine();
            writer.write("\\usepackage[T1]{fontenc}");
            writer.newLine();
            writer.write("\\usepackage{amsmath, amsfonts, amssymb}");
            writer.newLine();
            writer.write("\\begin{document}");
            writer.newLine();
            writer.write(prepareUnicodeForLaTeX("\\noindent Zadání:"));
            writer.newLine();
            writer.write(prepareForExport(problem));
            writer.newLine();
            writer.write("\\vfill");
            writer.newLine();
            writer.write(prepareUnicodeForLaTeX("\\noindent Řešení:"));
            writer.newLine();
            writer.write(prepareForExport(solution));
            writer.newLine();
            writer.write("\\end{document}");
        } catch(IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    private static String prepareForExport(final String input) {
        String tmp = input.trim().replaceAll(" +", " "); // Multiple whitespace
        if (input.contains("alignat"))
            tmp = tmp.replaceAll("alignat", "alignat*");
        else
            tmp = tmp.replaceAll("align", "align*");
        
        if (tmp.contains(" prvku")) {           // UNIT_GROUP_ELEMENT_ORDER
            tmp = tmp.replace("]\\\\ ", "]");   // Problem setting
            tmp = tmp.replaceAll("\\\\\\\\&", " "); // Fitting set elements to line
        }
        if (tmp.contains("Určete řád permutace ")) {  // PERM_ORDER
            tmp = tmp.replaceAll("\\$\\\\sigma =\\$ \\\\\\\\", "");
            tmp = tmp.replaceAll("\\\\\\[", "\\\\\\[\\\\sigma =");
        }
        return tmp;
    }
    
    /**
     * Use to escape characters with accents and carons in the output PDF document.
     * No need to use this for text inside \text{} tags.
     * @param input String
     * @return String with escaped characters
     */
    private static String prepareUnicodeForLaTeX(final String input) {
        String result = input.replaceAll("á", "\\\\'a");
        result = result.replaceAll("é", "\\\\'e");
        result = result.replaceAll("í", "\\\\'i");
        result = result.replaceAll("ó", "\\\\'o");
        result = result.replaceAll("ú", "\\\\'u");
        result = result.replaceAll("ý", "\\\\'y");
        
        result = result.replaceAll("č", "\\\\v{c}");
        result = result.replaceAll("ř", "\\\\v{r}");
        result = result.replaceAll("Ř", "\\\\v{R}");
        result = result.replaceAll("š", "\\\\v{s}");
        return result;
    }
    
    /**
     * Replaces last occurrence of a substring in a string with given replacement.
     * @param string Input string
     * @param substring Substring to be replaced
     * @param replacement Replacement of the substring
     * @return String with replacement or input string if it does not contain substring
     */
    public static String replaceLast(final String string,
            final String substring, final String replacement) {
        
        final int index = string.lastIndexOf(substring);
        if (index == -1)
            return string;
        
        final StringBuilder sb = new StringBuilder(string.substring(0, index));
        sb.append(replacement).append(string.substring(index + substring.length()));
        return sb.toString();
    }
    
    /**
     * @param i Cycle counter
     * @param terminator Cycle stop value
     * @return True e.g. when i == 9 in cycle: for (int i = 0; i < 10; ++i)
     */
    public static boolean lastForCycle(final int i, final int terminator) {
        return i == terminator - 1;
    }
}