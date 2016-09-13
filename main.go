package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
)

type Request struct {
	Message       string `json:"message"`
	Username      string `json:"username"`
	ReturnAddress string `json:"returnAddress"`
}

func handler(w http.ResponseWriter, r *http.Request) {
	if r.Method != "POST" {
		http.Error(w, "Unknown method", http.StatusMethodNotAllowed)
		return
	}

	if r.Header.Get("Content-Type") != "application/json" {
		http.Error(w, "Unsupported media type", http.StatusUnsupportedMediaType)
		return
	}

	req := Request{}
	err := json.NewDecoder(r.Body).Decode(&req)
	if err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	fmt.Fprintf(w, "%+v", req)
}

func main() {
	// First arg is executed command
	if len(os.Args) != 2 {
		fmt.Println("Usage: fag-sprak-crazychat <port>")
		return
	}

	ip := ""
	port := os.Args[1]
	http.HandleFunc("/", handler)
	http.ListenAndServe(ip+":"+port, nil)
}
