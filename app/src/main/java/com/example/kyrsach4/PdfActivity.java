package com.example.kyrsach4;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.infomaniak.lib.pdfview.PDFView;


public class PdfActivity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView = findViewById(R.id.pdfView);

        String fileName = getIntent().getStringExtra("PDF_NAME");

        pdfView.fromAsset("pdfs/" + fileName)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load();
    }
}
