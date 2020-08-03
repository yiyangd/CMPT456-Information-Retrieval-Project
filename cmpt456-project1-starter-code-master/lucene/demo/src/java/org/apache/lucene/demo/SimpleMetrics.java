/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;

public class SimpleMetrics {
  
  private IndexReader indexReader;
  
  public SimpleMetrics() {
    
  }
  
  public SimpleMetrics(IndexReader indexReader) {
    this.indexReader = indexReader;
  }

  public int getDocFre(Term term) throws IOException {
    return this.indexReader.docFreq(term);
  }
  
  
  public long getTermFreq(Term term) throws IOException {
    return this.indexReader.totalTermFreq(term);
  }
  
  public static void main(String[] args) throws Exception {
    String usage =
      "Usage:\tjava org.apache.lucene.demo.SimpleMetrics [-index dir] [-field f] [-term string] .";
    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
      System.out.println(usage);
      System.exit(0);
    }

    String index = "index";
    String field = "contents";
    String queryString = null;
    int hitsPerPage = 10;
    
    for(int i = 0;i < args.length;i++) {
      if ("-index".equals(args[i])) {
        index = args[i+1];
        i++;
      } else if ("-field".equals(args[i])) {
        field = args[i+1];
        i++;
      } else if ("-term".equals(args[i])) {
        queryString = args[i+1];
        i++;
      }
    }
    
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    SimpleMetrics sm = new SimpleMetrics(reader);
    System.out.println("Term: ");
    BufferedReader in = null;
    in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    while(true) {
      String input = in.readLine();
      if (input.length() == 0 || input.charAt(0) == 'n') {
        break;
      }
      Term term = new Term(field, input);
      int docFre = sm.getDocFre(term);
      long termFreq = sm.getTermFreq(term);
      System.out.println("Document frequency: " + docFre + ", The number of term: " + termFreq);
    }
    
    reader.close();
  }

}
