"""Generate a Stockfish Integration Plan PDF for SD2 Project Group-15."""

from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.lib.colors import HexColor, black, white
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak,
    ListFlowable, ListItem,
)
from reportlab.lib.enums import TA_LEFT, TA_JUSTIFY

OUTPUT = "/Users/maxshtymak/SD2_Project_Group-15/docs/Stockfish_Integration_Plan.pdf"

styles = getSampleStyleSheet()
DARK = HexColor("#2c3e50")
GREY = HexColor("#7f8c8d")
LIGHT = HexColor("#f8f9fa")
BORDER = HexColor("#bdc3c7")
ACCENT = HexColor("#2980b9")

h1 = ParagraphStyle("h1", parent=styles["Heading1"], fontSize=22,
                   textColor=DARK, spaceAfter=6, spaceBefore=4)
sub = ParagraphStyle("sub", parent=styles["Normal"], fontSize=11,
                     textColor=GREY, spaceAfter=14)
h2 = ParagraphStyle("h2", parent=styles["Heading2"], fontSize=15,
                   textColor=DARK, spaceBefore=18, spaceAfter=6)
h3 = ParagraphStyle("h3", parent=styles["Heading3"], fontSize=12,
                   textColor=HexColor("#34495e"), spaceBefore=10, spaceAfter=4)
body = ParagraphStyle("body", parent=styles["BodyText"], fontSize=10.5,
                      leading=15, alignment=TA_JUSTIFY, spaceAfter=6)
bullet = ParagraphStyle("bullet", parent=body, leftIndent=14, bulletIndent=4,
                        spaceAfter=3)
code = ParagraphStyle("code", parent=styles["Code"], fontSize=9,
                      leading=12, backColor=LIGHT, borderColor=BORDER,
                      borderWidth=0.5, borderPadding=6, leftIndent=0,
                      rightIndent=0, spaceAfter=8, spaceBefore=4)
note = ParagraphStyle("note", parent=body, backColor=HexColor("#fff8e1"),
                      borderColor=HexColor("#f5c518"), borderWidth=0.6,
                      borderPadding=6, spaceAfter=8)


def bullets(items):
    return ListFlowable(
        [ListItem(Paragraph(t, bullet), leftIndent=10) for t in items],
        bulletType="bullet", start="•", leftIndent=14,
    )


def section_table(rows, col_widths):
    t = Table(rows, colWidths=col_widths)
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), DARK),
        ("TEXTCOLOR", (0, 0), (-1, 0), white),
        ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
        ("FONTSIZE", (0, 0), (-1, -1), 9.5),
        ("ALIGN", (0, 0), (-1, 0), "CENTER"),
        ("VALIGN", (0, 0), (-1, -1), "TOP"),
        ("GRID", (0, 0), (-1, -1), 0.4, BORDER),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [white, LIGHT]),
        ("LEFTPADDING", (0, 0), (-1, -1), 6),
        ("RIGHTPADDING", (0, 0), (-1, -1), 6),
        ("TOPPADDING", (0, 0), (-1, -1), 5),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
    ]))
    return t


def build():
    doc = SimpleDocTemplate(
        OUTPUT, pagesize=A4,
        leftMargin=2 * cm, rightMargin=2 * cm,
        topMargin=1.8 * cm, bottomMargin=1.8 * cm,
        title="Stockfish Integration Plan",
        author="SD2 Project Group-15",
    )

    s = []

    # ----- Title -----
    s.append(Paragraph("Stockfish Integration Plan", h1))
    s.append(Paragraph("SD2 Project Group-15 &mdash; Iteration 3 &mdash; Chess Bot Opponent",
                       sub))

    # ----- 1. Overview -----
    s.append(Paragraph("1. Overview", h2))
    s.append(Paragraph(
        "For iteration 3 we will integrate the Stockfish chess engine to provide a "
        "computer opponent. Stockfish is an open-source UCI (Universal Chess Interface) "
        "engine: our Java application will launch the Stockfish executable as an external "
        "process and communicate with it by writing UCI commands to its stdin and reading "
        "the engine's responses from its stdout. The engine runs on a separate thread so "
        "the Swing UI stays responsive while the bot is thinking.",
        body,
    ))

    # ----- 2. Goals -----
    s.append(Paragraph("2. Goals &amp; Non-Goals", h2))
    s.append(Paragraph("<b>In scope:</b>", h3))
    s.append(bullets([
        "Launch Stockfish as a subprocess and communicate via the UCI protocol.",
        "Offer a &ldquo;Play vs Bot&rdquo; mode where one side is driven by the engine.",
        "Convert our internal board state into FEN / move-list so Stockfish can read it.",
        "Parse <i>bestmove</i> output, convert it back to <code>IndexPosition</code>, and play it on the board through <b>GameService.handleSquareClick</b>.",
        "Expose a difficulty setting (skill level and/or search depth / movetime).",
        "Handle engine lifecycle cleanly: start on new game, stop on window close.",
    ]))
    s.append(Paragraph("<b>Out of scope for this iteration:</b>", h3))
    s.append(bullets([
        "Opening book, endgame tablebases, multi-PV analysis view.",
        "Online multiplayer; bundling a full GUI analysis board.",
        "Shipping the Stockfish binary itself &mdash; the user installs it locally.",
    ]))

    # ----- 3. Architecture -----
    s.append(Paragraph("3. Architecture", h2))
    s.append(Paragraph(
        "We will add a new <b>Services/BotService</b> class that owns the engine "
        "subprocess, plus a small <b>FenService</b> (or static helper) that converts "
        "our <code>Piece[][]</code> board into FEN. <b>GameService</b> stays the single "
        "authority for moves; the bot only calls the same <code>handleSquareClick</code> "
        "entry-point the user does. This keeps rules (check, castling, en&nbsp;passant, "
        "promotion) in one place.",
        body,
    ))

    arch_rows = [
        ["Component", "Responsibility", "Status"],
        ["Game / Board", "UI + user input (Swing). Unchanged.", "existing"],
        ["GameService", "Rules, move execution, turn tracking.", "existing"],
        ["CheckService / DrawService", "Check, checkmate, stalemate, draws.", "existing"],
        ["BotService", "Owns Stockfish process; sends UCI, reads bestmove; "
                       "asynchronously returns the chosen move.", "NEW"],
        ["FenService", "Converts current Piece[][] + side-to-move + "
                       "castling rights + en passant target into a FEN string.", "NEW"],
        ["UciMoveParser", "Converts bestmove strings like e2e4 / e7e8q into "
                          "IndexPosition (+ promotion piece).", "NEW"],
        ["BotController (in Game)", "Listens for end-of-turn, asks BotService "
                                    "for a move, applies it on the EDT.", "NEW"],
    ]
    s.append(section_table(arch_rows, [3.2 * cm, 9 * cm, 2.3 * cm]))

    s.append(Paragraph(
        "<b>Data flow (bot&rsquo;s turn):</b> user makes move &rarr; "
        "<code>handleSquareClick</code> updates board &rarr; turn flips to bot color "
        "&rarr; <code>BotController</code> calls <code>BotService.requestMove(fen, depth)</code> "
        "on a worker thread &rarr; Stockfish replies <code>bestmove e7e5</code> &rarr; "
        "parser produces <code>IndexPosition(4,4)</code> &rarr; "
        "<code>SwingUtilities.invokeLater</code> replays that move through the "
        "same <code>handleSquareClick</code> path used by the human.",
        body,
    ))

    s.append(PageBreak())

    # ----- 4. UCI crash course -----
    s.append(Paragraph("4. UCI Protocol &mdash; What We Actually Need", h2))
    s.append(Paragraph(
        "UCI is line-based text. We only need a small subset:",
        body,
    ))
    s.append(bullets([
        "<code>uci</code> &mdash; handshake; engine replies with id/options, then <code>uciok</code>.",
        "<code>isready</code> &mdash; engine replies <code>readyok</code>; used as a sync point.",
        "<code>ucinewgame</code> &mdash; reset engine state between games.",
        "<code>setoption name Skill Level value N</code> &mdash; difficulty (0&ndash;20).",
        "<code>position fen &lt;FEN&gt;</code> or <code>position startpos moves e2e4 ...</code>.",
        "<code>go depth 10</code> or <code>go movetime 1000</code> &mdash; start searching.",
        "<code>bestmove &lt;move&gt;</code> &mdash; engine&rsquo;s reply we care about.",
        "<code>quit</code> &mdash; ask engine to exit cleanly.",
    ]))
    s.append(Paragraph("Example session we will send/receive:", h3))
    s.append(Paragraph(
        "&gt; uci<br/>"
        "&lt; id name Stockfish 16 ... uciok<br/>"
        "&gt; setoption name Skill Level value 5<br/>"
        "&gt; ucinewgame<br/>"
        "&gt; isready<br/>"
        "&lt; readyok<br/>"
        "&gt; position fen rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1<br/>"
        "&gt; go movetime 1000<br/>"
        "&lt; bestmove e7e5",
        code,
    ))

    # ----- 5. BotService sketch -----
    s.append(Paragraph("5. BotService &mdash; Implementation Sketch", h2))
    s.append(Paragraph(
        "Core skeleton (details will live in <code>src/Services/BotService.java</code>):",
        body,
    ))
    s.append(Paragraph(
        "public class BotService {<br/>"
        "&nbsp;&nbsp;private Process engine;<br/>"
        "&nbsp;&nbsp;private BufferedReader in;<br/>"
        "&nbsp;&nbsp;private BufferedWriter out;<br/>"
        "<br/>"
        "&nbsp;&nbsp;public void start(String exePath) throws IOException { ... }<br/>"
        "&nbsp;&nbsp;public void setSkillLevel(int level) { send(&quot;setoption name Skill Level value &quot; + level); }<br/>"
        "&nbsp;&nbsp;public String requestBestMove(String fen, int movetimeMs) { ... }<br/>"
        "&nbsp;&nbsp;public void newGame() { send(&quot;ucinewgame&quot;); waitReady(); }<br/>"
        "&nbsp;&nbsp;public void shutdown() { send(&quot;quit&quot;); engine.destroy(); }<br/>"
        "<br/>"
        "&nbsp;&nbsp;private void send(String cmd) { out.write(cmd + &quot;\\n&quot;); out.flush(); }<br/>"
        "&nbsp;&nbsp;private void waitReady() { send(&quot;isready&quot;); readUntil(&quot;readyok&quot;); }<br/>"
        "}",
        code,
    ))
    s.append(Paragraph(
        "<b>Important:</b> all blocking I/O with Stockfish must run on a worker thread "
        "(e.g. <code>SwingWorker</code> or a single-thread <code>ExecutorService</code>). "
        "Doing it on the Swing Event Dispatch Thread will freeze the window while the "
        "engine searches.",
        note,
    ))

    # ----- 6. FEN conversion -----
    s.append(Paragraph("6. FEN Conversion &mdash; Mapping Our Board to Stockfish", h2))
    s.append(Paragraph(
        "Stockfish does not know about our <code>Piece</code> classes, so we build a FEN string. "
        "A FEN has six fields: piece placement, side to move, castling rights, en passant target, "
        "halfmove clock, fullmove number.",
        body,
    ))
    s.append(Paragraph("<b>Mapping rules:</b>", h3))
    s.append(bullets([
        "Rows: FEN starts from rank 8 (row index 7 in our array) and ends at rank 1. Iterate <code>row = 7 &rarr; 0</code>.",
        "Columns: left-to-right is file a&rarr;h, i.e. col 0&rarr;7.",
        "Pieces: <code>King&rarr;K, Queen&rarr;Q, Rook&rarr;R, Bishop&rarr;B, Knight&rarr;N, Pawn&rarr;P</code>. "
        "Uppercase for WHITE, lowercase for BLACK.",
        "Side to move: read <code>GameService.getCurrentColorToMove()</code> &rarr; &ldquo;w&rdquo; or &ldquo;b&rdquo;.",
        "Castling rights: derive from <code>canCastleKingSide</code>/<code>canCastleQueenSide</code>, "
        "or walk <code>moveStorage</code> to see if king/rook has moved; output KQkq as appropriate, or &ldquo;-&rdquo;.",
        "En passant target: if <code>getLastDoubleStepPawn()</code> is non-null, compute the square behind it (e.g. &ldquo;e3&rdquo;); else &ldquo;-&rdquo;.",
        "Halfmove clock: DrawService already tracks 50-move rule &mdash; reuse that counter.",
        "Fullmove number: <code>(moveCounter + 1) / 2</code>.",
    ]))

    s.append(PageBreak())

    # ----- 7. Parsing bestmove -----
    s.append(Paragraph("7. Parsing &ldquo;bestmove&rdquo;", h2))
    s.append(Paragraph(
        "Stockfish returns moves in long algebraic notation, e.g. <code>e2e4</code>, "
        "<code>e1g1</code> (castling), <code>e7e8q</code> (promotion). The converter:",
        body,
    ))
    s.append(bullets([
        "Characters 0&ndash;1 &rarr; from-square; characters 2&ndash;3 &rarr; to-square.",
        "File letter &rarr; column (<code>a=0 ... h=7</code>); rank digit &rarr; row (<code>&lsquo;1&rsquo;=0 ... &lsquo;8&rsquo;=7</code>).",
        "Optional 5th char = promotion piece (q, r, b, n). Stash it; "
        "<code>GameService.executePromotion</code> currently auto-queens, so we will "
        "extend it to accept the chosen piece.",
        "Feed the resulting <code>IndexPosition</code> into <code>handleSquareClick</code> "
        "<b>twice</b>: first on the from-square (selects the piece), then on the to-square "
        "(performs the move) &mdash; exactly like a human click sequence.",
    ]))

    # ----- 8. UI / UX changes -----
    s.append(Paragraph("8. UI / UX Changes", h2))
    s.append(bullets([
        "New menu / dialog at startup: &ldquo;Human vs Human&rdquo; vs &ldquo;Human vs Bot&rdquo;, "
        "plus a choice of which color the human plays.",
        "Difficulty slider (Skill Level 0&ndash;20, or movetime 100&ndash;3000 ms).",
        "&ldquo;Bot is thinking...&rdquo; label or disabled board while Stockfish searches "
        "(prevents the user from clicking during the bot&rsquo;s turn).",
        "Path-to-engine field (with a sane default) &mdash; the user tells us where "
        "<code>stockfish</code> is installed; store it in a small config file.",
        "Graceful error dialog if the executable is missing or the handshake fails.",
    ]))

    # ----- 9. Testing -----
    s.append(Paragraph("9. Testing Strategy", h2))
    s.append(bullets([
        "<b>Unit</b>: <code>FenServiceTest</code> &mdash; build several known board states "
        "(starting position, after 1.e4, mid-game with castling rights lost, en passant available) "
        "and assert the exact FEN string.",
        "<b>Unit</b>: <code>UciMoveParserTest</code> &mdash; parse e2e4, e1g1, e7e8q, a7a8n &rarr; "
        "verify from/to indices and promotion piece.",
        "<b>Unit (mocked engine)</b>: <code>BotServiceTest</code> &mdash; inject a fake "
        "<code>BufferedReader</code>/<code>BufferedWriter</code> pair scripted with canned UCI "
        "responses so we can assert protocol correctness without launching Stockfish.",
        "<b>Integration</b>: a single slow test that actually starts Stockfish, plays "
        "the opening move from startpos, and asserts a legal response. Guarded by "
        "<code>Assume.assumeTrue(new File(path).canExecute())</code> so CI doesn&rsquo;t fail "
        "for teammates who haven&rsquo;t installed the engine.",
        "<b>Manual</b>: full game against the bot at 3 skill levels; verify castling, "
        "en&nbsp;passant, promotion, checkmate detection, resign/new-game.",
    ]))

    # ----- 10. Work breakdown -----
    s.append(Paragraph("10. Work Breakdown &amp; Ownership", h2))
    wb_rows = [
        ["#", "Task", "Deliverable", "Est."],
        ["1", "Install Stockfish on each dev machine; document path in README.",
         "README section + config file format.", "0.5 d"],
        ["2", "Implement FenService + unit tests.",
         "FenService.java, FenServiceTest.java", "1 d"],
        ["3", "Implement UciMoveParser + unit tests.",
         "UciMoveParser.java, UciMoveParserTest.java", "0.5 d"],
        ["4", "Implement BotService (process I/O, UCI handshake, requestBestMove).",
         "BotService.java + mocked tests", "1.5 d"],
        ["5", "Wire BotController into Game: worker thread, "
              "EDT callback, &lsquo;bot thinking&rsquo; state.",
         "Changes in Game.java / Board.java", "1 d"],
        ["6", "Extend executePromotion to accept piece type "
              "(needed for e7e8q from engine).",
         "GameService.java update + tests", "0.5 d"],
        ["7", "Add new-game dialog: mode + color + difficulty + engine path.",
         "Small Swing dialog class", "1 d"],
        ["8", "Integration test + manual test pass at 3 difficulty levels.",
         "Test log in PR description", "0.5 d"],
        ["9", "Write iteration-3 report section describing the integration.",
         "PDF / docs update", "0.5 d"],
    ]
    s.append(section_table(wb_rows,
                           [0.8 * cm, 5.8 * cm, 6.4 * cm, 1.6 * cm]))

    # ----- 11. Risks -----
    s.append(Paragraph("11. Risks &amp; Mitigations", h2))
    risk_rows = [
        ["Risk", "Mitigation"],
        ["Stockfish binary path differs per OS / machine.",
         "Config file + env var fallback; clear error dialog if not found."],
        ["Swing UI freezes while engine searches.",
         "All engine I/O on a worker thread; update board via invokeLater."],
        ["FEN generator disagrees with engine&rsquo;s legal-move set.",
         "Unit tests against known positions; cross-check with an online FEN tool during dev."],
        ["Promotion mismatch &mdash; engine picks knight, we auto-queen.",
         "Extend executePromotion to accept a piece type; default stays queen."],
        ["Engine process leaks if window is force-closed.",
         "Register shutdown hook / windowClosing listener that calls BotService.shutdown()."],
        ["Castling / en passant info missing from FEN breaks engine.",
         "Dedicated unit tests covering both flags; use DrawService&rsquo;s halfmove counter."],
    ]
    s.append(section_table(risk_rows, [6.2 * cm, 8.3 * cm]))

    # ----- 12. Definition of Done -----
    s.append(Paragraph("12. Definition of Done", h2))
    s.append(bullets([
        "Human can start a new game, choose color + difficulty, and play a full game against Stockfish to checkmate, stalemate or draw.",
        "All new classes have JUnit5 tests; existing test suite still passes.",
        "Closing the window terminates the Stockfish process (verified via Activity Monitor).",
        "README documents how to install Stockfish and where to point the engine path.",
        "Iteration-3 report includes this integration and the final class diagram.",
    ]))

    doc.build(s)
    print(f"Wrote {OUTPUT}")


if __name__ == "__main__":
    build()