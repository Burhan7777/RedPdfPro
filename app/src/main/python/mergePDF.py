from pypdf import PdfReader, PdfWriter, PdfMerger


def merge_pdf(list_of_pdf, name):
    pdf_writer = PdfWriter()
    for i in list_of_pdf:
        pdf_writer.append(i)

    try:
        pdf_writer.write(f"/storage/emulated/0/Download/Pro Scanner/Pdfs/{name}.pdf")
        return "Success"
    except Exception as e:
        return "Failure"
