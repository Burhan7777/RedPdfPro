import requests
from requests.auth import HTTPBasicAuth


def make_request(source_path, target_format_1):
    api_key = '5c060a983bb5ab4f480ed985908b6f07d8c5e2ec'
    endpoint = "https://api.zamzar.com/v1/jobs"
    source_file = source_path
    target_format = target_format_1

    file_content = {'source_file': open(source_file, 'rb')}
    data_content = {'target_format': target_format}
    res = requests.post(endpoint, data=data_content, files=file_content, auth=HTTPBasicAuth(api_key, ''),verify=False)
    return res.json()


def check_status(job_id):
    job_id = job_id
    api_key = '5c060a983bb5ab4f480ed985908b6f07d8c5e2ec'
    endpoint = f"https://api.zamzar.com/v1/jobs/{job_id}".format(job_id)

    response = requests.get(endpoint, auth=HTTPBasicAuth(api_key, ''))

    return response.json()


def download_file(file_id, file_path):
    file_id = file_id
    local_filename = file_path
    api_key = '5c060a983bb5ab4f480ed985908b6f07d8c5e2ec'
    endpoint = f"https://api.zamzar.com/v1/files/{file_id}/content".format(file_id)

    response = requests.get(endpoint, stream=True, auth=HTTPBasicAuth(api_key, ''))
    chunk_size = 1024

    try:
        with open(local_filename, 'wb') as f:
            for chunk in response.iter_content(chunk_size=chunk_size):
                if chunk:
                    f.write(chunk)
                    f.flush()

        return "Success"

    except IOError:
        return "Failure"

