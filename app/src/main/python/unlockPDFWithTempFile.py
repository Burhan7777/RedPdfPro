from PyPDF2 import PdfReader, PdfWriter


def unlock_pdf_temp_file(file, password, name):
    reader = PdfReader(file)
    writer = PdfWriter()

    if reader.is_encrypted:
        reader.decrypt(password)

    # Add all pages to the writer
    for page in reader.pages:
        writer.add_page(page)

    # Save the new PDF to a file
    try:
        writer.write(f"/storage/emulated/0/Download/Pro Scanner/temp/{name}.pdf")
        return "Success"
    except Exception as e:
        return f"Failure: {str(e)}"
