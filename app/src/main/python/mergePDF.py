from pypdf import PdfReader, PdfWriter, PdfMerger


def merge_pdf(list_of_pdf, name):
    pdf_writer = PdfWriter()
    for i in list_of_pdf:
        pdf_writer.append(i)

    pdf_writer.write(f"/storage/emulated/0/Download/{name}.pdf")
