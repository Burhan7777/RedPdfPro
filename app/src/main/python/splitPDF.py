from pypdf import PdfReader, PdfWriter


def total_pages(file, password=""):
    reader = PdfReader(file)
    if reader.is_encrypted:
        reader = PdfReader(file, password=password)
    try:
        return len(reader.pages)
    except Exception:
        return "PDFlocked"


def split(file, list, name):
#    if len(unlocked_pdfreader.pages) > 0:
  #      reader = unlocked_pdfreader
   # else:
    reader = PdfReader(file)
    writer = PdfWriter()
    pages = reader.pages
    for i in list:
        writer.add_page(pages[i])

    try:
        writer.write(f"/storage/emulated/0/Download/Pro Scanner/Pdfs/{name}.pdf")
        return "Success"
    except Exception:
        return "Failure"


def unlock_pdf(file, password):
    global unlocked_pdfreader
    reader = PdfReader(file)

    if reader.is_encrypted:
        reader.decrypt(password)

    return total_pages(file)


def is_encrypted(file):
    reader = PdfReader(file)
    return reader.is_encrypted
