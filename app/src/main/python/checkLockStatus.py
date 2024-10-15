from PyPDF2 import PdfReader, PdfWriter


def check_lock_status_pdf(file):
    reader = PdfReader(file)
    writer = PdfWriter()

    if reader.is_encrypted:
        return "Locked"
    else:
        return "Unlocked"