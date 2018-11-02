# MineSweeper

SPECYFIKACJE

1. Okna aplikacji
	1.1. Okno menu
		- ustawianie wielkości planszy
		- ustawianie ilości min na planszy
		- ustawianie autoodkrywania obszarów
		- przycisk "Nowa gra"
	1.2. Okno gry
		1.2.1. Statystyki
			- ikona + czas pozostały do końca gry
			- ikona + liczba pozostałych do znalezienia na planszy min
			- ikona + punktacja użytkownika za grę
		1.2.2. Plansza gry
			- matryca jednakowych pól
			- każde pole jest klikalne
				a) LPM - odkrycie pola
				b) PPM - oznaczenie pola flagą
					Flaga blokuje pole przed niezamierzonym kliknięciem LPM i redukuje liczbę pozostałych do okdrycia bomb.
			- pole może zawierać minę lub nie
			- każde pole, które nie zawiera miny posiada przypisaną liczbę przylegających do niego pól zawierających miny

2. Przebieg gry
	1) Uruchomienie
		Użytkownik uruchamia aplikację. Na ekranie pojawia się okno menu (1.1.), 
		w którym użytkownik ustawia parametry gry i klikając przycisk "Nowa gra" - uruchamia ja.
	2) Przygotowanie
		Program generuje planszę o zadanych wymiarach i parametrach i otwiera ją w osobnym oknie (1.2.).
		Równocześnie okno menu zostaje wyłączone do momentu zakończenia danej rozgrywki.
		Po otwarciu się okna gry użytkownik widzi ile ma czasu na znalezienie jakiej liczby min oraz, że posiada 0 punktów.
		W momencie kliknięcia LPM w dowolne pole na planszy program losowo rozstawia miny po planszy (omijając już kliknięte pole).
		W ten sposób pierwsze kliknięcie nigdy nie będzie kliknięciem w minę co jest równoznaczne z przegraną.
	3) Gra
		Kiedy pole zostało kliknięte a miny rozstawione licznik czasu zaczyna odliczanie.
		Pozostały czas jest na bierząco wyświetlany na górze okna.
		Kiedy pole zostaje oznaczone flagą (PPM) zmniejsza się liczba pozostałych do znaleznienia min.
		Gra może się zakończyć na 3 sposoby:
			a) WYGRANA - Gracz oznaczył wszystkie pola z minami flagą i odkliknął wszystkie pozostałe pola.
			b) PRZEGRANA - Gracz kliknął pole z miną.
			c) PAS - Gracz zamknął okno gry klikając przycisk "X".
		Po zakończeniu rozgrywki i zamknięciu okna gry, okno menu zostaje przywrócone.
		Użytkownik może teraz ustawić i rozpocząć kolejne podejście.
