from pypdf import PdfReader, PdfWriter


def rotate_pdf(file, angle, pages_selected, name):
    reader = PdfReader(file)
    writer = PdfWriter()
    pages = reader.pages
    for page in pages_selected:
        writer.add_page(pages[page])

    for page in range(len(writer.pages)):
        writer.pages[page].rotate(angle)

    try:
        writer.write(f"/storage/emulated/0/Download/Pro Scanner/Pdfs/{name}.pdf")
        return "Success"
    except Exception:
        return "Failure"
