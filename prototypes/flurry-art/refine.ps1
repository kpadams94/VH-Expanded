Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot;$im=[Drawing.Bitmap]::new((Join-Path $root 'native/01-color.png'))
$edits=@(@{x=3;y=13;rgba='FFF2BCFF'},@{x=6;y=11;rgba='FFF2BCFF'},@{x=9;y=11;rgba='FFF2BCFF'},@{x=6;y=8;rgba='FFDF79FF'},@{x=9;y=9;rgba='FFDF79FF'},@{x=6;y=1;rgba='F95449FF'},@{x=9;y=3;rgba='F95449FF'},@{x=0;y=14;rgba='00000000'},@{x=1;y=15;rgba='00000000'},@{x=2;y=14;rgba='00000000'})
foreach($e in $edits){$hex=$e.rgba;$im.SetPixel($e.x,$e.y,[Drawing.Color]::FromArgb([Convert]::ToInt32($hex.Substring(6,2),16),[Convert]::ToInt32($hex.Substring(0,2),16),[Convert]::ToInt32($hex.Substring(2,2),16),[Convert]::ToInt32($hex.Substring(4,2),16)))}
$im.Save((Join-Path $root 'native/flurry-v1.png'),[Drawing.Imaging.ImageFormat]::Png)
$rows=@();for($y=0;$y -lt 16;$y++){$row=@();for($x=0;$x -lt 16;$x++){$p=$im.GetPixel($x,$y);$row+=('{0:X2}{1:X2}{2:X2}{3:X2}' -f $p.R,$p.G,$p.B,$p.A)};$rows+=,@($row)}
@{width=16;height=16;format='RGBA hex rows';pixels=$rows;editorEdits=$edits;provenance='Native-grid drawing and replay of ten recorded Piskel edits.'}|ConvertTo-Json -Depth 8|Set-Content -Encoding utf8 (Join-Path $root 'native/flurry-v1.source.json')
$b=[Drawing.Bitmap]::new(780,440);$g=[Drawing.Graphics]::FromImage($b);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode='NearestNeighbor';$g.PixelOffsetMode='Half';$font=[Drawing.Font]::new('Segoe UI',12);$small=[Drawing.Font]::new('Segoe UI',10)
$g.DrawString('FLURRY / 16 x 16 draft',$font,[Drawing.Brushes]::White,24,16);$g.DrawString('Actual PNG at 1x, 3x and 16x (nearest-neighbor)',$small,[Drawing.Brushes]::White,24,47)
$g.DrawImage($im,[Drawing.Rectangle]::new(25,91,16,16));$g.DrawImage($im,[Drawing.Rectangle]::new(74,79,48,48));$g.DrawImage($im,[Drawing.Rectangle]::new(24,155,256,256))
$g.FillRectangle([Drawing.Brushes]::LightGray,310,155,256,256);$g.DrawImage($im,[Drawing.Rectangle]::new(310,155,256,256))
$g.DrawString('Approved family / 3x',$small,[Drawing.Brushes]::White,595,80)
foreach($entry in @(@('Lunge','../../docs/art/lunge/lunge.png',140),@('Impact','../../docs/art/impact/impact.png',245),@('Siphon','../../docs/art/siphon/siphon.png',350))){$ref=[Drawing.Bitmap]::new((Join-Path $root $entry[1]));$g.DrawString($entry[0],$small,[Drawing.Brushes]::White,610,($entry[2]-25));$g.DrawImage($ref,[Drawing.Rectangle]::new(610,$entry[2],48,48));$ref.Dispose()}
$g.Dispose();$b.Save((Join-Path $root 'flurry-v1-review.png'));$b.Dispose();$im.Dispose()

