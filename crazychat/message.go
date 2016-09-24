package crazychat

//go:generate gorx --import=github.com/mesan/fag-sprak-crazychat/crazychat -o rx/rx.go rx crazychat.Message

type Message struct {
	Message       string `json:"message"`
	Username      string `json:"username"`
	ReturnAddress string `json:"returnAddress"`
}
