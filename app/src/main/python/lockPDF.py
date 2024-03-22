from PyPDF2 import PdfReader, PdfWriter


def lock_pdf(file, password, name):
    reader = PdfReader(file)
    writer = PdfWriter()

    # Add all pages to the writer
    for page in reader.pages:
        writer.add_page(page)

    # Add a password to the new PDF
    writer.encrypt(password)

    # Save the new PDF to a file
    try:
        with open(f"/storage/emulated/0/Download/{name}.pdf", "wb") as f:
            writer.write(f)
        return "Success"
    except Exception:
        return "Failure"
