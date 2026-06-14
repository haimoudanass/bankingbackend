package com.yassirrijali.bankingbackend.ai.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Value("classpath:/pdfs/banque-reglementation.pdf")
    private Resource pdfResource;

    @Bean
    public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public ApplicationRunner loadData(SimpleVectorStore vectorStore) {
        return args -> {
            try {
                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
                TokenTextSplitter textSplitter = new TokenTextSplitter();
                List<Document> documents = textSplitter.apply(pdfReader.get());

                vectorStore.add(documents);
                log.info("RAG : {} segments indexes depuis banque-reglementation.pdf", documents.size());
            } catch (Exception e) {
                log.error("Indexation RAG echouee — verifiez votre cle OpenAI dans application-local.properties", e);
            }
        };
    }
}
