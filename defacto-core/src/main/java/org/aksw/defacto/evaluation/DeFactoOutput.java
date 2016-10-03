package org.aksw.defacto.evaluation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.aksw.defacto.Defacto;
import org.aksw.defacto.evidence.ComplexProof;
import org.aksw.defacto.evidence.Evidence;
import org.aksw.defacto.model.DefactoModel;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by dnes on 20/07/16.
 */
public class DeFactoOutput {


    private static Logger logger = Logger.getLogger(DeFactoOutput.class);


    public DeFactoOutput(){

    }

    public static void main(String[] args) throws Exception {


        String path_spouse = "/Users/dnes/Github/FactBench/v1/test/correct/spouse";
        List<DefactoModel> models = new ArrayList<DefactoModel>();


        try {

            File dir = new File(path_spouse);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File fbfile : directoryListing) {

                    Model model = ModelFactory.createDefaultModel();
                    model.read(new FileReader(fbfile), "", "TTL");
                    boolean isCorrect = true;

                    models.add(new DefactoModel(model, fbfile.getName(), isCorrect, Arrays.asList("en","de","fr")));

                }
            }

            List<DefactoModel> models2 = new ArrayList<DefactoModel>();
            models2.add(models.get(0));

            Map<DefactoModel, Evidence> evidences =
                    Defacto.checkFacts(models2, Defacto.TIME_DISTRIBUTION_ONLY.NO);

            //DefactoModel anterior = evidences.get(0).getModel();

            FileWriter fw = new FileWriter("defactoevidences.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            for (Map.Entry<DefactoModel, Evidence> entry : evidences.entrySet())
            {

                out.print(entry.getKey().getName().toString() + ";");
                out.print(entry.getKey().getSubjectUri() + ";");
                out.print(entry.getKey().getPropertyUri() + ";");
                out.print(entry.getKey().getObjectUri() + ";");
                out.print(String.valueOf(entry.getKey().isCorrect()) + ";");
                out.print(entry.getValue().getDeFactoScore() + ";");

                int cpsize = entry.getValue().getComplexProofs().size();
                int i = 1;
                for(ComplexProof p: entry.getValue().getComplexProofs()){
                    out.print(p.getWebSite().getUrl() + ";");
                    out.print(p.getWebSite().getScore() + ";");
                    out.print(p.getWebSite().getTopicCoverageScore() + ";");
                    out.print(p.getWebSite().getTopicMajorityWebFeature() + ";");
                    out.print(p.getWebSite().getTopicMajoritySearchFeature() + ";");
                    out.print(p.getWebSite().getPageRankScore() + ";");
                    out.print(p.getWebSite().getPageRank() + ";");
                    out.println(p.getWebSite().getQuery().toString() + ";");
                    if (i<cpsize)
                        out.print(";;;;;;");
                    i++;
                }

                out.println();
                out.flush();

            }


        }catch (Exception e){
            System.out.print(e.toString());
        }


    }






}
