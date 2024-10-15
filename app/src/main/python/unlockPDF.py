from PyPDF2 import PdfReader, PdfWriter


def unlock_pdf(file, password, name):
    reader = PdfReader(file)
    writer = PdfWriter()

    if reader.is_encrypted:
        reader.decrypt(password)

    # Add all pages to the writer
    for page in reader.pages:
        writer.add_page(page)

    # Save the new PDF to a file
    try:
        writer.write(f"/storage/emulated/0/Download/Pro Scanner/Pdfs/{name}.pdf")
        return "Success"
    except Exception:
        return "Failure"
