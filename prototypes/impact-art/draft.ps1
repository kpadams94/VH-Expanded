Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
$variants=@(
@('.......#........','.......#........','.......#........','.....#####......','....#..#..#.....','.....#####......','.......#........','...#########....','..#....#....#...','...#########....','.......#........','..###########...','.#.....#.....#..','..#...###...#...','...#########....','................'),
@('.......#........','.......#........','.....#####......','....#..#..#.....','.....#####......','.......#........','....#######.....','..##...#...##...','...#########....','.......#........','.......#........','...#########....','..#....#....#...','.#....###....#..','..###########...','................'),
@('.......#........','.......#........','......###.......','.....#.#.#......','......###.......','.......#........','....#######.....','...#...#...#....','....#######.....','.......#........','...#########....','..#....#....#...','.#.....#.....#..','..#...###...#...','...#########....','................'))
for($v=0;$v -lt 3;$v++){
 $im=[Drawing.Bitmap]::new(16,16);for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){if($variants[$v][$y][$x] -eq '#'){$im.SetPixel($x,$y,[Drawing.ColorTranslator]::FromHtml('#FFF2BC'))}}};$im.Save((Join-Path $root "native/0$($v+1).png"));$im.Dispose()
}
$board=[Drawing.Bitmap]::new(720,310);$g=[Drawing.Graphics]::FromImage($board);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode='NearestNeighbor';$g.PixelOffsetMode='Half';$font=[Drawing.Font]::new('Segoe UI',11)
for($v=0;$v -lt 3;$v++){$im=[Drawing.Bitmap]::new((Join-Path $root "native/0$($v+1).png"));$ox=20+240*$v;$g.DrawString("0$($v+1)",$font,[Drawing.Brushes]::White,$ox,10);$g.DrawImage($im,[Drawing.Rectangle]::new($ox,40,16,16));$g.DrawImage($im,[Drawing.Rectangle]::new($ox+45,35,48,48));$g.DrawImage($im,[Drawing.Rectangle]::new($ox,95,192,192));$im.Dispose()};$g.Dispose();$board.Save((Join-Path $root 'silhouettes.png'));$board.Dispose()
