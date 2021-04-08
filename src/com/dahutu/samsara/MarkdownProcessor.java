package com.dahutu.samsara;

/*
Problem:
We're going to build the beginnings of a markdown processor. Markdown is a markup language that allows you
to easily create HTML.

We ll provide some sample input and desired output. Don't worry too much about edge cases, but feel free to ask
if you re unsure or think there s something we ought to consider.

Part 1:  A markdown processor is capable of handling a multitude of string to html tag formats. For now, we just
want to focus on supporting <p/>, <br/>, <blockquote/>, and <del/> tags.

Input:
String input = "This is a paragraph with a soft\n" + "line break.\n\n" + "This is another paragraph that has\n" +
               "> Some text that\n" + "> is in a\n" + "> block quote.\n\n" +
			   "This is another paragraph with a ~~strikethrough~~ word.";

Expected Output:
"<p>This is a paragraph with a soft<br />line break.</p>
<p>This is another paragraph that has <br />
<blockquote>Some text that<br />is in a<br />
block quote.</blockquote> </p> <p>This is another paragraph with a
<del>strikethrough</del> word.</p>"
*/

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownProcessor {

  public static final String input = "This is a paragraph with a soft\n" +
          "line break.\n\n" +
          "This is another paragraph that has\n" +
          "> Some text that\n" + "> is in a\n" +
          "> block quote.\n\n" +
          "This is another paragraph with a ~~strikethrough~~ word.";

  public static void main(String[] args) {
    MarkdownProcessor s = new MarkdownProcessor();
    System.out.println(s.convert(input));
  }

  public String convert(String input) {
    StringBuilder sb = new StringBuilder();

    String[] paragraphs = input.split("\n\n");
    for (String p : paragraphs) {
      sb.append("<p>").append(convertParagraph(p)).append("</p>");
    }

    return sb.toString();
  }

  String convertParagraph(String input) {
    // handle blockquote
    StringBuilder sb = new StringBuilder();
    String[] parts = input.split("\n>\s+");
    sb.append(parts[0]);
    if (parts.length > 1) { // has blockquote
      sb.append("\n<blockquote>");
      for (int i = 1; i < parts.length; i++) {
          sb.append("\n").append(parts[i]);
      }
      sb.append("</blockquote>");
    }

    // handle soft line break
    String s1 = sb.toString().replaceAll("\n", "<br />");

    // handle strikethrough
    sb = new StringBuilder();
    parts = s1.split("~~");
    sb.append(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      sb.append(i % 2 == 0 ? "</del>" : "<del>");
      sb.append(parts[i]);
    }

    return sb.toString();
  }
}
