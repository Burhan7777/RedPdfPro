from pypdf import PdfReader, PdfWriter


def total_pages(file):
    reader = PdfReader(file)
    return len(reader.pages)


def split(file, list, name):
    reader = PdfReader(file)
    writer = PdfWriter()
    pages = reader.pages
    for i in list:
        writer.add_page(pages[i])

    try:
        writer.write(f"/storage/emulated/0/Download/{name}.pdf")
        return "Success"
    except Exception:
        return "Failure"

