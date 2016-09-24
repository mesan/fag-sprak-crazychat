package crazychat

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

type MessageCallback func(Message)

func ListenForMessages(port string, callback MessageCallback) {
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != "POST" {
			http.Error(w, "Unknown method", http.StatusMethodNotAllowed)
			return
		}

		if r.Header.Get("Content-Type") != "application/json" {
			http.Error(w, "Unsupported media type", http.StatusUnsupportedMediaType)
			return
		}

		msg := Message{}
		err := json.NewDecoder(r.Body).Decode(&msg)
		if err != nil {
			http.Error(w, "Invalid JSON", http.StatusBadRequest)
			return
		}

		fmt.Fprintf(w, "%#v", msg)

		callback(msg)
	})
	go http.ListenAndServe(":"+port, nil)
}

func SendMessage(msg Message, address string) {
	buf := bytes.Buffer{}
	json.NewEncoder(&buf).Encode(msg)
	http.Post("http://"+address, "application/json", &buf)
}
