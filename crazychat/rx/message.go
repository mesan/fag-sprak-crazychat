package rx

//go:generate gorx -o rx.go rx Message

type Message struct {
	Message       string `json:"message"`
	Username      string `json:"username"`
	ReturnAddress string `json:"returnAddress"`
}
