import socket
import sys

HOST = ''
PORT = int(sys.argv[1])
MODEL_NAME = sys.argv[2]
MODEL_PATH = sys.argv[3]
SCRIPT = f'PYTHON CLASSIFIER: PORT {PORT},' \
         f' MODEL_NAME: {MODEL_NAME}, MODEL_PATH: {MODEL_PATH}'
print(f'{SCRIPT} started')

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen(1)
    conn, addr = s.accept()
    with conn:
        print(f'{SCRIPT}: connected by', addr)
        while True:
            data = conn.recv(1024)
            if not data:
                print(f'{SCRIPT}: message with no data')
                break
            else:
                message = data.decode('utf-8')
                print(f'{SCRIPT}: message: {message}')
                match message:
                    case "hi":
                        conn.sendall(f'{message}\n'.encode())
                    case 'bye':
                        conn.sendall(f'{message}\n'.encode())
                    case _:
                        res = False if 'qwerty' in message else True
                        conn.sendall(f'{str(res)}\n'.encode())
