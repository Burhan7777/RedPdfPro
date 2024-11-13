package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.FeatureBox

@Composable
fun HomePage(navHostController: NavHostController, viewModel: MyViewModel) {
    viewModel.modelList.clear()
    viewModel.listOfPdfToMerge.clear()
    viewModel.pdfNames.clear()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = stringResource(id = R.string.tools),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            // viewModel.mutableStateListOfPdfs.clear()
            //viewModel.listOfPdfNames.clear()

            FeatureBox(
                stringId = R.string.splitPdf,
                drawableId = R.drawable.split,
                contentDescription = stringResource(id = R.string.splitPdf),
                navHostController,
                Screens.SplitPdf.route,
            )

            FeatureBox(
                stringId = R.string.mergePdf,
                drawableId = R.drawable.merge,
                contentDescription = stringResource(id = R.string.mergePdf),
                navHostController,
                Screens.MergePdf.route
            )
            FeatureBox(
                stringId = R.string.rotatePDF,
                drawableId = R.drawable.rotate,
                contentDescription = stringResource(id = R.string.rotatePDF),
                navHostController,
                Screens.RotatePdf.route
            )
            FeatureBox(
                stringId = R.string.lockPDF,
                drawableId = R.drawable.lock,
                contentDescription = stringResource(id = R.string.lockPDF),
                navHostController,
                Screens.LockPdf.route
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureBox(
                stringId = R.string.unlockPDF,
                drawableId = R.drawable.unlock,
                contentDescription = stringResource(id = R.string.unlockPDF),
                navHostController,
                Screens.UnlockPdf.route
            )
        }
        // Spacer(modifier = Modifier.height(10.dp))
//        Text(
//            text = stringResource(id = R.string.ocr),
//            color = MaterialTheme.colorScheme.onPrimary,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            FeatureBox(
//                stringId = R.string.scanToDocx,
//                drawableId = R.drawable.scan,
//                contentDescription = stringResource(id = R.string.scanToDocx),
//                navHostController,
//                Screens.ScanToDocx.route,
//            )
//            FeatureBox(
//                stringId = R.string.scanToTxt,
//                drawableId = R.drawable.scan,
//                contentDescription = stringResource(id = R.string.scanToTxt),
//                navHostController,
//                Screens.ScanToTxt.route,
//            )
//        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.extractFromPdf),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            FeatureBox(
                stringId = R.string.extractText,
                drawableId = R.drawable.text,
                contentDescription = stringResource(id = R.string.extractText),
                navHostController,
                Screens.ExtractText.route,
            )
            FeatureBox(
                stringId = R.string.extractImage,
                drawableId = R.drawable.image,
                contentDescription = stringResource(id = R.string.extractImage),
                navHostController,
                Screens.ExtractImageFromPdf.route,
            )

        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.convertTOPDF),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            FeatureBox(
                stringId = R.string.imagesToPDF,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.imagesToPDF),
                navHostController,
                Screens.ImageToPdf.route,
            )
            FeatureBox(
                stringId = R.string.docToPdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.docToPdf),
                navHostController,
                Screens.DocToPdf.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.docsToPDF,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.docsToPDF),
                navHostController,
                Screens.DocsToPdf.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pptToPdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.pptToPdf),
                navHostController,
                Screens.PptToPdf.route,
                R.drawable.ic_premium
            )


        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureBox(
                stringId = R.string.pptxtopdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.docsToPDF),
                navHostController,
                Screens.PptxToPdf.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.xlsToPdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.xlsToPdf),
                navHostController,
                Screens.XlsToPdf.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.xlsxToPdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.xlsxToPdf),
                navHostController,
                Screens.XlsxToPdf.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.csvToPdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.csvToPdf),
                navHostController,
                Screens.CsvToPdf.route,
                R.drawable.ic_premium
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureBox(
                stringId = R.string.epubToPdf,
                drawableId = R.drawable.converttopdf,
                contentDescription = stringResource(id = R.string.epubToPdf),
                navHostController,
                Screens.EpubToPdf.route,
                R.drawable.ic_premium
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.convertFromPdf),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureBox(
                stringId = R.string.pdfToJpg,
                drawableId = R.drawable.image,
                contentDescription = stringResource(id = R.string.pdfToJpg),
                navHostController,
                Screens.PdfToJpgScreen.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pdfToDocx,
                drawableId = R.drawable.word_red,
                contentDescription = stringResource(id = R.string.convertToDocx),
                navHostController,
                Screens.PdfToDocxScreen.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pdfToPpt,
                drawableId = R.drawable.pptx_image,
                contentDescription = stringResource(id = R.string.pdfToPpt),
                navHostController,
                Screens.PdfToPptScreen.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pdfToPptx,
                drawableId = R.drawable.pptx_image,
                contentDescription = stringResource(id = R.string.pdfToPptx),
                navHostController,
                Screens.PdfToPptxScreen.route,
                R.drawable.ic_premium
            )
//            FeatureBox(
//                stringId = R.string.pdfToDoc,
//                drawableId = R.drawable.word_red,
//                contentDescription = stringResource(id = R.string.pdfToDoc),
//                navHostController,
//                Screens.PdfToDocScreen.route,
//                R.drawable.ic_premium
//            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureBox(
                stringId = R.string.pdfToEpub,
                drawableId = R.drawable.epub_image,
                contentDescription = stringResource(id = R.string.pdfToEpub),
                navHostController,
                Screens.PdfToEpubScreen.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pdfToCsv,
                drawableId = R.drawable.csv_image,
                contentDescription = stringResource(id = R.string.pdfToCsv),
                navHostController,
                Screens.PdfToCsvScreen.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pdfToXls,
                drawableId = R.drawable.xls_image,
                contentDescription = stringResource(id = R.string.pdfToXls),
                navHostController,
                Screens.PdfToXls.route,
                R.drawable.ic_premium
            )
            FeatureBox(
                stringId = R.string.pdfToXlsx,
                drawableId = R.drawable.xlsx_image,
                contentDescription = stringResource(id = R.string.pdfToXlsx),
                navHostController,
                Screens.PdfToXlsx.route,
                R.drawable.ic_premium
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            FeatureBox(
//                stringId = R.string.pdftohtml5,
//                drawableId = R.drawable.html_image,
//                contentDescription = stringResource(id = R.string.pdftohtml5),
//                navHostController,
//                Screens.PdfToHtml5.route,
//                R.drawable.ic_premium
//            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            stringResource(R.string.convertToDocx),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureBox(
                stringId = R.string.docToDocx,
                drawableId = R.drawable.word_red,
                contentDescription = stringResource(id = R.string.docToDocx),
                navHostController,
                Screens.DocToDOcx.route,
                R.drawable.ic_premium
            )
        }
    }
}