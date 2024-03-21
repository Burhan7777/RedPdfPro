from pypdf import PdfReader, PdfWriter


def compress_pdf(file, name):
    reader = PdfReader(file)
    writer = PdfWriter()

    for page in reader.pages:
        writer.add_page(page)

    for page in writer.pages:
        page.compress_content_streams()  # This is CPU intensive!

    try:
        with open(f"/storage/emulated/0/Download/{name}.pdf", "wb") as f:
            writer.write(f)

        return "Success"

    except Exception:
        return "Failure"
